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

package tech.ordinaryroad.barrage.fly.route

import cn.dev33.satoken.stp.StpUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.router
import tech.ordinaryroad.barrage.fly.handler.BarrageFlyUserHandler

@Configuration(proxyBeanMethods = false)
class BarrageFlyUserRoutingConfiguration {

    @Bean
    fun userRouterFunction(handler: BarrageFlyUserHandler): RouterFunction<ServerResponse> {
        return coRouter {
            "/user".nest {
                POST("/login", accept(MediaType.TEXT_PLAIN), handler::login)
            }
        }
    }

    @Bean
    fun userMonoRouterFunction(handler: BarrageFlyUserHandler): RouterFunction<ServerResponse> {
        return router {
            "/user".nest {
                before {
                    StpUtil.checkLogin()
                    it
                }
                GET("/info", handler::info)
                GET("/logout", accept(MediaType.TEXT_PLAIN), handler::logout)
            }
        }
    }

}