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

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import tech.ordinaryroad.barrage.fly.handler.BarrageFlyAppHandler

@Configuration(proxyBeanMethods = false)
class BarrageFlyAppRoutingConfiguration {

    @Bean
    fun appMonoRouterFunction(handler: BarrageFlyAppHandler): RouterFunction<ServerResponse> {
        return router {
            "/app".nest {
                GET("/configurations", handler::configurations)
            }
        }
    }

    companion object {
        private val ACCEPT_JSON = RequestPredicates.accept(MediaType.APPLICATION_JSON)
    }

}