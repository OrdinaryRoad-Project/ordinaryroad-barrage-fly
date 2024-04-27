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

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import tech.ordinaryroad.barrage.fly.property.AdminProperties
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil

@Component
class BarrageFlyAppHandler(val adminProperties: AdminProperties) {

    private val log = LoggerFactory.getLogger(BarrageFlyAppHandler::class.java)

    fun configurations(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().bodyValue(
            OrJacksonUtil.getInstance().createObjectNode().apply {
                put("rsaPublicKey", adminProperties.rsaPublicKey)
            }
        )
    }
}