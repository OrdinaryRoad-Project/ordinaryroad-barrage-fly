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

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.asymmetric.KeyType
import com.fasterxml.jackson.databind.node.ObjectNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.publisher.Mono
import tech.ordinaryroad.barrage.fly.dto.UserDTO
import tech.ordinaryroad.barrage.fly.dto.UserInfoDTO
import tech.ordinaryroad.barrage.fly.property.AdminProperties
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil

@Component
class BarrageFlyUserHandler(private val adminProperties: AdminProperties) {

    private val log = LoggerFactory.getLogger(BarrageFlyUserHandler::class.java)

    suspend fun login(request: ServerRequest): ServerResponse {
        val body = request.awaitBody(String::class)
        val rsa = SecureUtil.rsa(adminProperties.rsaPrivateKey, adminProperties.rsaPublicKey)
        val jsonNode = OrJacksonUtil.getInstance().readTree(rsa.decrypt(body, KeyType.PrivateKey)) as ObjectNode
        val username = jsonNode.get("username").asText()
        val password = jsonNode.get("password").asText()
        val remember = jsonNode.get("remember").asBoolean()
        val matched = adminProperties.username == username && adminProperties.password == password
        return if (matched) {
            StpUtil.login(username, remember)
            ServerResponse.ok()
                .bodyValueAndAwait(
                    OrJacksonUtil.getInstance().createObjectNode().apply {
                        putPOJO("userInfo", UserInfoDTO(UserDTO(username)))
                        put("timeout", StpUtil.getTokenTimeout())
                    }
                )
        } else {
            ServerResponse.badRequest().bodyValueAndAwait("用户名或密码错误")
        }
    }

    fun info(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().bodyValue(
            UserInfoDTO(UserDTO(StpUtil.getLoginIdAsString()))
        )
    }

    fun logout(request: ServerRequest): Mono<ServerResponse> {
        StpUtil.logout()
        return ServerResponse.ok().build()
    }

}