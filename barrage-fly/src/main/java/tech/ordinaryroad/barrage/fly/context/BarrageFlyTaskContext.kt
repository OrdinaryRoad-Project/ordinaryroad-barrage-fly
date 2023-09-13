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
package tech.ordinaryroad.barrage.fly.context

import cn.hutool.cache.impl.CacheObj
import cn.hutool.cache.impl.TimedCache
import cn.hutool.core.lang.mutable.Mutable
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.fasterxml.jackson.databind.node.ObjectNode
import org.slf4j.LoggerFactory
import org.springframework.messaging.rsocket.RSocketRequester
import tech.ordinaryroad.barrage.fly.constant.BarrageFlyTaskStatusEnum
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO
import tech.ordinaryroad.barrage.fly.listener.BilibiliMsgListener
import tech.ordinaryroad.barrage.fly.listener.DouyuMsgListener
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg.OBJECT_MAPPER
import tech.ordinaryroad.live.chat.client.commons.base.msg.IDanmuMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.IGiftMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.commons.client.BaseLiveChatClient
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener
import tech.ordinaryroad.live.chat.client.servers.netty.client.config.BaseNettyClientConfig
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap

/**
 * 每个任务的上下文
 *
 * @author mjz
 * @date 2023/9/10
 */
class BarrageFlyTaskContext(
    val platform: PlatformEnum,
    val roomId: String,
    val taskId: String,
    /**
     * TODO 变化时重启
     */
    var clientConfigJson: String,
    /**
     * 消息队列大小
     */
    var MSG_QUEUE_MAX_SIZE :Int= 100,

    /**
     * 每次可以消费的消息个数
     */
    var CONSUME_MSG_MIN_SIZE :Int= 10,
    var CONSUME_MSG_PERIOD: Long = 1000L * CONSUME_MSG_MIN_SIZE / MSG_QUEUE_MAX_SIZE
) {

    /**
     * 一个任务多个客户端，每个客户端对应一个Channel
     */
    val rSocketClientMsgQueues = ConcurrentHashMap<Int, ArrayBlockingQueue<ObjectNode>>()
    private val log = LoggerFactory.getLogger(BarrageFlyTaskContext::class.java)

    private var client: BaseLiveChatClient<*>? = null

    var status = BarrageFlyTaskStatusEnum.NEW
        set(status) {
            val oldStatus = field
            field = status
            statusChangeSupport.firePropertyChange("status", oldStatus, status)
        }
    var statusChangeSupport = PropertyChangeSupport(status)

    private val clientStatusChangeListener = PropertyChangeListener {
        this.status = when (it.newValue) {
            ClientStatusEnums.CONNECTING -> {
                BarrageFlyTaskStatusEnum.CONNECTING
            }

            ClientStatusEnums.RECONNECTING -> {
                BarrageFlyTaskStatusEnum.RECONNECTING
            }

            ClientStatusEnums.CONNECTED -> {
                BarrageFlyTaskStatusEnum.RUNNING
            }

            ClientStatusEnums.CONNECT_FAILED -> {
                BarrageFlyTaskStatusEnum.CONNECT_FAILED
            }

            ClientStatusEnums.DISCONNECTED -> {
                BarrageFlyTaskStatusEnum.DISCONNECTED
            }

            else -> {
                BarrageFlyTaskStatusEnum.STOPPED
            }
        }
    }

    fun destroy() {
        log.debug("destroy context")
        rSocketClientMsgQueues.clear()
        for (connectedClient in connectedClients) {
            connectedClient.dispose()
        }
        connectedClients.clear()
        client?.let {
            it.destroy()
            client = null
        }
        status = BarrageFlyTaskStatusEnum.STOPPED
    }

    fun start() {
        log.debug("start client")
        (client ?: createClient(platform, clientConfigJson)).let {
            it.addStatusChangeListener(clientStatusChangeListener)
            it.connect()
            this.client = it
        }
    }

    fun createClientConfig(platform: PlatformEnum, clientConfigJson: String): BaseNettyClientConfig {
        return when (platform) {
            PlatformEnum.bilibili -> {
                OBJECT_MAPPER.readValue(clientConfigJson, BilibiliLiveChatClientConfig::class.java)
            }

            PlatformEnum.douyu -> {
                OBJECT_MAPPER.readValue(clientConfigJson, DouyuLiveChatClientConfig::class.java)
            }
        }
    }

    fun createMsgListener(platform: PlatformEnum): IBaseMsgListener<*, *, *, *> {
        return when (platform) {
            PlatformEnum.bilibili -> {
                SpringUtil.getBean(BilibiliMsgListener::class.java)
            }

            PlatformEnum.douyu -> {
                SpringUtil.getBean(DouyuMsgListener::class.java)
            }
        }
    }

    fun createClient(platform: PlatformEnum, clientConfigJson: String): BaseLiveChatClient<*> {
        val msgListener = createMsgListener(platform)
        val clientConfig = createClientConfig(platform, clientConfigJson)

        return when (platform) {
            PlatformEnum.bilibili -> {
                BilibiliLiveChatClient(
                    clientConfig as BilibiliLiveChatClientConfig,
                    msgListener as IBilibiliMsgListener
                )
            }

            PlatformEnum.douyu -> {
                DouyuLiveChatClient(clientConfig as DouyuLiveChatClientConfig, msgListener as IDouyuMsgListener)
            }
        }
    }

    /**
     * 发布消息到每个队列
     * TODO 消息过滤
     */
    fun publishMsg(platform: PlatformEnum, roomId: String, msg: IMsg) {
        if (connectedClients.isEmpty) {
            // 没有连接的客户端忽略，并尝试清空队列
            if (rSocketClientMsgQueues.size > 0) {
                rSocketClientMsgQueues.clear()
            }
            return
        }

        val objectNode = OBJECT_MAPPER.createObjectNode().apply {
            put("platform", platform.name)
            put("roomId", roomId)
            if (msg is IDanmuMsg) {
                put("type", "danmu")
            } else if (msg is IGiftMsg) {
                put("type", "gift")
            }
            putPOJO("msg", msg)
        }
        rSocketClientMsgQueues.forEach { (_, queue) ->
            val success = queue.offer(objectNode)
            // 队列已经满了，但还未来得及消费（一般不会出现这种情况）
            if (!success) {
                queue.clear()
                if (log.isWarnEnabled) {
                    log.warn(
                        "the msg queue is full, clear msg queue, number of connected clients: {}",
                        connectedClients.size()
                    )
                }
            }
        }
    }

    /**
     * 每个Channel对应一个消息队列
     */
    fun registerChannel(requester: RSocketRequester) {
        val hashCode = requester.hashCode()
        rSocketClientMsgQueues[hashCode] = ArrayBlockingQueue(MSG_QUEUE_MAX_SIZE)
    }

    fun unregisterChannel(requester: RSocketRequester) {
        val hashCode = requester.hashCode()
        rSocketClientMsgQueues.remove(hashCode)
    }

    companion object {
        val connectedClients = TimedCache(
            24 * 3600 * 1000L, ConcurrentHashMap<Mutable<Int>, CacheObj<Int, RSocketRequester>>()
        )

        /**
         * <TaskId,BarrageFlyTaskContext>
         */
        val taskContexts = ConcurrentHashMap<String, BarrageFlyTaskContext>()

        /**
         * 任务启动时注册上下文，未注册时新建
         */
        fun getOrCreateContext(barrageFlyTaskDO: BarrageFlyTaskDO): BarrageFlyTaskContext {
            val clientConfigJson = (OBJECT_MAPPER.readTree(
                StrUtil.blankToDefault(
                    barrageFlyTaskDO.clientConfigJson,
                    StrUtil.EMPTY_JSON
                )
            ) as ObjectNode).apply {
                put("roomId", barrageFlyTaskDO.roomId)
                put("cookie", barrageFlyTaskDO.cookie)
            }.toString()
            return taskContexts.getOrPut(barrageFlyTaskDO.uuid) {
                BarrageFlyTaskContext(
                    barrageFlyTaskDO.platform,
                    barrageFlyTaskDO.roomId,
                    barrageFlyTaskDO.uuid,
                    clientConfigJson
                )
            }.apply {
                this.clientConfigJson = clientConfigJson
            }
        }

        /**
         * 任务终止时移除上下文
         */
        fun removeContext(taskId: String) {
            taskContexts[taskId]?.destroy()
        }

        fun getContext(taskId: String?): BarrageFlyTaskContext? {
            if (taskId == null) {
                return null
            }
            return taskContexts[taskId]
        }

        fun getContexts(taskId: List<String>): List<BarrageFlyTaskContext> {
            if (taskId.isEmpty()) {
                return emptyList()
            }
            return taskContexts.filterKeys {
                taskId.contains(it)
            }.map {
                it.value
            }
        }

        fun getContexts(platform: PlatformEnum, roomId: String): List<BarrageFlyTaskContext> {
            return taskContexts.filterValues { it.platform == platform && it.roomId == roomId }
                .map { it.value }
        }

        fun destroy() {
            taskContexts.forEach { (_, v) -> v.destroy() }
        }

        fun getTaskStatus(taskId: String): BarrageFlyTaskStatusEnum {
            val context = getContext(taskId) ?: return BarrageFlyTaskStatusEnum.STOPPED
            return context.status
        }

        /**
         * 保存新连接的客户端
         */
        fun registerClient(requester: RSocketRequester) {
            val hashCode = requester.hashCode()
            connectedClients.put(hashCode, requester)
        }

        /**
         * 注销客户端
         */
        fun unregisterClient(requester: RSocketRequester) {
            val hashCode = requester.hashCode()
            connectedClients.remove(hashCode)
        }

        /**
         * 注册Channel
         */
        fun registerChannels(taskIds: List<String>, requester: RSocketRequester) {
            getContexts(taskIds).forEach {
                it.registerChannel(requester)
            }
        }

        /**
         * 关闭指定的Channel
         */
        fun unregisterChannels(taskIds: List<String>, requester: RSocketRequester) {
            getContexts(taskIds).forEach {
                it.unregisterChannel(requester)
            }
        }

        /**
         * 关闭所有Channel
         */
        fun unregisterAllChannels(requester: RSocketRequester) {
            taskContexts.forEach { (_, v) ->
                v.unregisterChannel(requester)
            }
        }
    }
}
