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

import cn.hutool.core.util.StrUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import tech.ordinaryroad.barrage.fly.handler.BarrageFlyStatsHandler
import tech.ordinaryroad.barrage.fly.property.ApiProperties

@Configuration(proxyBeanMethods = false)
class BarrageFlyStatsRoutingConfiguration(val apiProperties: ApiProperties) {

//    @Bean
//    fun statsRouterFunction(handler: BarrageFlyStatsHandler): RouterFunction<ServerResponse> {
//        return coRouter {
//            "/stats".nest {
////                POST("", ACCEPT_JSON, userHandler::createTask)
////                DELETE("/{id}", ACCEPT_JSON, userHandler::deleteTask)
////                PUT("/clientConfigJson/{id}", accept(MediaType.TEXT_PLAIN), userHandler::updateClientConfigJson)
//            }
//        }
//    }

    @Bean
    fun statsMonoRouterFunction(handler: BarrageFlyStatsHandler): RouterFunction<ServerResponse> {
        return router {
            "${StrUtil.emptyIfNull(apiProperties.prefix)}/stats".nest {
                GET("/counts", handler::counts)
            }
        }
    }

    companion object {
        private val ACCEPT_JSON = RequestPredicates.accept(MediaType.APPLICATION_JSON)
    }

}