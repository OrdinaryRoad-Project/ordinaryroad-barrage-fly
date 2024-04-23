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
package tech.ordinaryroad.barrage.fly.config

import cn.hutool.log.LogFactory
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Configuration


/**
 * @author mjz
 * @date 2024/4/19
 */
@Configuration
class WebConfig(val serverProperties: ServerProperties) {

    private val log = LogFactory.get()

// TODO Spring Boot Webflux静态资源有问题，按钮跳转可以正常访问，刷新后就404了
//    @Bean
//    @ConditionalOnExpression("T(cn.hutool.core.util.StrUtil).isNotBlank('\${server.servlet.context-path}')")
//    fun contextPathWebFilter(): WebFilter {
////        val contextPath = serverProperties.servlet.contextPath
//        val contextPath = "/api"
//        log.info("ContextPathWebFilter Enabled, ContextPathPrefix: $contextPath")
//        return WebFilter { exchange, chain ->
//            val request = exchange.request
//            return@WebFilter if (request.uri.getPath().startsWith(contextPath)) {
//                chain.filter(
//                    exchange.mutate()
//                        .request(
//                            request.mutate()
//                                // .contextPath(contextPath)
//                                 .path(request.uri.getPath().replaceFirst(contextPath, ""))
////                                .path(request.uri.getPath())
//                                .build()
//                        )
//                        .build()
//                )
//            } else {
//                chain.filter(exchange)
//            }
//        }
//    }

}
