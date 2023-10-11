/*
 * Copyright 2023 OrdinaryRoad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.ordinaryroad.barrage.fly.message

import cn.hutool.http.HttpStatus
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import tech.ordinaryroad.barrage.fly.context.BarrageFlyTaskContext
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO
import tech.ordinaryroad.barrage.fly.dto.msg.BarrageFlyMsgDTO
import tech.ordinaryroad.barrage.fly.express.BarrageFlyExpressContext
import tech.ordinaryroad.barrage.fly.express.BarrageFlyExpressRunner
import tech.ordinaryroad.barrage.fly.listener.ExampleMsgPublisher
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg.OBJECT_MAPPER
import java.util.stream.Collectors

/**
 *
 *
 * @author mjz
 * @date 2023/9/10
 */
@Suppress("UNCHECKED_CAST")
@Controller
class BarrageController(private val expressRunner: BarrageFlyExpressRunner) {

    private val log = LoggerFactory.getLogger(BarrageController::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    @ConnectMapping("")
    fun connect(setupPayload: JsonNode?, requester: RSocketRequester) {
        if (log.isDebugEnabled) {
            log.debug("on connect {}, setupPayload {}", requester.hashCode(), setupPayload)
        }
        GlobalScope.launch {
            requester.rsocketClient().source()
                .publishOn(Schedulers.boundedElastic())
                // 连接建立成功
                .doOnNext {
                    BarrageFlyTaskContext.registerClient(requester)
                    // 连接关闭监听
                    it.onClose()
                        .doFinally {
                            if (log.isDebugEnabled) {
                                log.debug("onClose finally {}", requester.hashCode())
                            }
                            // 连接被关闭
                            BarrageFlyTaskContext.unregisterClient(requester)
                        }
                        .subscribe()
                }
                .subscribe()
        }
    }

    @MessageMapping("")
    fun channel(datas: Flux<JsonNode>, requester: RSocketRequester): Flux<Any> {
        if (log.isDebugEnabled) {
            log.debug("on channel {} {}", requester.hashCode(), requester)
        }
        val subscribedTaskIds: HashSet<String> = HashSet()
        return datas
            .doFirst {
                if (log.isDebugEnabled) {
                    log.debug("channel first {}", requester.hashCode())
                }
            }
            .doOnNext {
                val cmd = it.get("cmd").asText()
                val taskIds = it.withArray<JsonNode>("taskIds").map { jsonNode -> jsonNode.asText() }
                if (log.isDebugEnabled) {
                    log.debug("channel on next {}, payload {}", requester.hashCode(), it)
                }
                // 收到client发送的订阅请求
                if (cmd == "SUBSCRIBE") {
                    subscribedTaskIds.addAll(taskIds)
                    BarrageFlyTaskContext.registerChannels(subscribedTaskIds.toList(), requester)
                }
                // 收到client发送的取消订阅请求
                else if (cmd == "UNSUBSCRIBE") {
                    subscribedTaskIds.removeAll(taskIds.toSet())
                    BarrageFlyTaskContext.unregisterChannels(taskIds, requester)
                }
            }
            .doOnError {
                log.error("channel on error", it)
            }
            .doOnCancel {
                // channel关闭
                if (log.isDebugEnabled) {
                    log.debug("channel on cancel {}", requester.hashCode())
                }
            }
            .doFinally {
                if (log.isDebugEnabled) {
                    log.debug("channel finally {}", requester.hashCode())
                }
                BarrageFlyTaskContext.unregisterAllChannels(requester)
            }
            .switchMap {
                val cmd = it.get("cmd").asText()
                if (cmd == "SUBSCRIBE" && BarrageFlyTaskContext.getContexts(subscribedTaskIds.toList()).isEmpty()) {
                    Flux.just(
                        OBJECT_MAPPER.createObjectNode().apply {
                            put("status", HttpStatus.HTTP_BAD_REQUEST)
                            put("message", "the task $subscribedTaskIds don't have contexts yet")
                        }
                    )
                } else {
                    val hashCode = requester.hashCode()
                    val toList = subscribedTaskIds.stream().map { taskId ->
                        val context = BarrageFlyTaskContext.getContext(taskId)!!
                        val publisher = context.rSocketClientMsgPublishers[hashCode]!!
                        val roomId = context.barrageFlyTaskDO.roomId
                        val expressContext = BarrageFlyExpressContext()
                        Flux.from(publisher)
                            .map { msg ->
                                expressContext.setMsg(BarrageFlyMsgDTO(roomId, msg))
                                expressContext.getMsg()
                            }
                            .map {
                                // 前置操作
                                val result = expressRunner.executePreMapExpress(
                                    context.barrageFlyTaskDO.msgPreMapExpress,
                                    expressContext
                                )
                                expressContext.setMsg(result)
                                result
                            }
                            .filterWhen { result ->
                                Flux.just(result)
                                    .map {
                                        // 过滤
                                        expressRunner.executeFilterExpress(
                                            context.barrageFlyTaskDO.msgFilterExpress,
                                            expressContext
                                        )
                                    }
                            }
                            .map {
                                // 后置操作
                                val result = expressRunner.executePostMapExpress(
                                    context.barrageFlyTaskDO.msgPostMapExpress,
                                    expressContext
                                )
                                expressContext.setMsg(result)
                                result
                            }
                    }.collect(Collectors.toList())
                    val just = Flux.just(OBJECT_MAPPER.createObjectNode().apply {
                        put("status", HttpStatus.HTTP_OK)
                        put("message", "ok")
                    }) as Flux<Any>
                    if (toList.isEmpty()) {
                        just
                    } else {
                        just.mergeWith(Flux.merge(toList))
                    }
                }
            }
    }

    @ConnectMapping("example")
    fun exampleConnect(setupPayload: JsonNode?, requester: RSocketRequester) {
        if (log.isDebugEnabled) {
            log.debug("example on connect {}, setupPayload {}", requester.hashCode(), setupPayload)
        }
    }

    @MessageMapping("example")
    fun exampleChannel(datas: Flux<JsonNode>, requester: RSocketRequester): Flux<Any> {
        return datas
            .map {
                val taskNode = it.get("task") as ObjectNode
                taskNode.remove("id")?.let {
                    taskNode.put("uuid", it.asText())
                    }
                OBJECT_MAPPER.readValue(taskNode.toString(), BarrageFlyTaskDO::class.java)
            }
            .switchMap { barrageFlyTaskDO ->
                val expressContext = BarrageFlyExpressContext()
                Flux.from(ExampleMsgPublisher())
                    .map { msg ->
                        expressContext.setMsg(msg)
                        expressContext.getMsg()
                    }
                    .map {
                        // 前置操作
                        val result = expressRunner.executePreMapExpress(
                            barrageFlyTaskDO.msgPreMapExpress,
                            expressContext
                        )
                        expressContext.setMsg(result)
                        result
                    }
                    .filterWhen { result ->
                        Flux.just(result)
                            .map {
                                // 过滤
                                expressRunner.executeFilterExpress(
                                    barrageFlyTaskDO.msgFilterExpress,
                                    expressContext
                                )
                            }
                    }
                    .map {
                        // 后置操作
                        val result = expressRunner.executePostMapExpress(
                            barrageFlyTaskDO.msgPostMapExpress,
                            expressContext
                        )
                        expressContext.setMsg(result)
                        result
                    }
            }
    }
}