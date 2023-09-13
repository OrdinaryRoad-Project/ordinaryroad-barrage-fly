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

package tech.ordinaryroad.barrage.fly.handler

import cn.hutool.core.util.EnumUtil
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.ordinaryroad.barrage.fly.constant.BarrageFlyTaskStatusEnum
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum
import tech.ordinaryroad.barrage.fly.context.BarrageFlyTaskContext
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO
import tech.ordinaryroad.barrage.fly.dto.BarrageFlyTaskDTO.Companion.toDTO
import tech.ordinaryroad.barrage.fly.service.BarrageFlyTaskService
import tech.ordinaryroad.commons.core.base.request.query.BaseQueryRequest
import tech.ordinaryroad.commons.mybatis.utils.PageUtils
import kotlin.jvm.optionals.getOrNull

@Component
class BarrageFlyTaskHandler(private val barrageFlyTaskService: BarrageFlyTaskService) {

    private val log = LoggerFactory.getLogger(BarrageFlyTaskHandler::class.java)

    suspend fun createTask(request: ServerRequest): ServerResponse {
        val task = request.awaitBody<BarrageFlyTaskDO>()
        val create = barrageFlyTaskService.create(task)
        return ServerResponse.ok().bodyValueAndAwait(create.toDTO())
    }

    suspend fun deleteTask(request: ServerRequest): ServerResponse {
        val id = request.queryParam("id").get()
        val delete = barrageFlyTaskService.delete(id)
        BarrageFlyTaskContext.removeContext(id)
        return ServerResponse.ok().bodyValueAndAwait(delete)
    }

    suspend fun updateCookie(request: ServerRequest): ServerResponse {
        val id = request.queryParam("id").get()
        val string = request.awaitBody<String>()

        barrageFlyTaskService.findById(id) ?: return ServerResponse.notFound().buildAndAwait()

        val barrageFlyTaskDO = BarrageFlyTaskDO()
        barrageFlyTaskDO.uuid = id
        barrageFlyTaskDO.cookie = string

        val update = barrageFlyTaskService.updateSelective(barrageFlyTaskDO)

        return ServerResponse.ok().bodyValueAndAwait(update.toDTO())
    }

    fun getTask(request: ServerRequest): Mono<ServerResponse> {
        val id = request.queryParam("id").get()
        val findById = barrageFlyTaskService.findById(id)
        return ServerResponse.ok().bodyValue(findById.toDTO())
    }

    fun getTaskStatus(request: ServerRequest): Mono<ServerResponse> {
        val id = request.queryParam("id").get()
        val status = BarrageFlyTaskContext.getTaskStatus(id)
        return ServerResponse.ok().bodyValue(status)
    }

    fun pageTask(request: ServerRequest): Mono<ServerResponse> {
        val offset = request.pathVariable("offset").toInt()
        val limit = request.pathVariable("limit").toInt()
        val sortBy = request.exchange().request.queryParams["sortBy"] ?: emptyList()
        val sortDesc = request.exchange().request.queryParams["sortDesc"] ?: emptyList()
        val platform = request.queryParam("platform").getOrNull()
        val roomId = request.queryParam("roomId").getOrNull()

        val baseQueryRequest = BaseQueryRequest()
            .apply {
                this.offset = offset
                this.limit = limit
                this.sortBy = sortBy.toTypedArray()
                this.sortDesc = sortDesc.map { it.toBoolean() }.toTypedArray()
            }

        PageHelper.offsetPage<BarrageFlyTaskDO>(offset, limit)
        val all = barrageFlyTaskService.findAll(
            BarrageFlyTaskDO().apply {
                if (!platform.isNullOrBlank()) {
                    this.platform = EnumUtil.fromString(PlatformEnum::class.java, platform)
                }
                if (!roomId.isNullOrBlank()) {
                    this.roomId = roomId
                }
            }, baseQueryRequest
        ) as Page<BarrageFlyTaskDO>

        val page = PageUtils.pageInfoDo2PageInfoDto(all) { it.toDTO() }

        return ServerResponse.ok().bodyValue(page)
    }

    fun start(request: ServerRequest): Mono<ServerResponse> {
        val ids = request.queryParam("ids").stream().toList()

        return ServerResponse.ok().body(
            Flux.fromIterable(barrageFlyTaskService.findIds(ids))
                .doOnNext {
                    BarrageFlyTaskContext.getOrCreateContext(it).start()
                }
                .map {
                    BarrageFlyTaskContext.getTaskStatus(it.uuid)
                }
        )
    }

    fun stop(request: ServerRequest): Mono<ServerResponse> {
        val ids = request.queryParam("ids").stream().toList()
        ids.forEach { id -> BarrageFlyTaskContext.removeContext(id) }
        return ServerResponse.ok().build()
    }

    fun statuses(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .body(
                Flux.fromArray(BarrageFlyTaskStatusEnum.entries.toTypedArray())
                    .map {
                        HashMap<String, String>(2).apply {
                            this["label"] = it.name
                            this["value"] = it.name
                        }
                    }
            )
    }
}