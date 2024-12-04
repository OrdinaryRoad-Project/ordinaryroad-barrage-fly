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

import cn.dev33.satoken.basic.SaBasicUtil
import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.reactor.filter.SaReactorFilter
import cn.dev33.satoken.router.SaHttpMethod
import cn.dev33.satoken.router.SaRouter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse

/**
 * [Sa-Token 权限认证] 全局配置类
 */
@Configuration
class SaTokenConfigure {

    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    fun saReactorFilter(): SaReactorFilter = SaReactorFilter().apply {
        addInclude("/admin/**")
            .setAuth {
                SaBasicUtil.check()
            }
        setError {
            return@setError ServerResponse.status(HttpStatus.UNAUTHORIZED)
        }

        // 前置函数：在每次认证函数之前执行
        setBeforeAuth {
            SaHolder.getResponse()
                // ---------- 设置跨域响应头 ----------
                // 允许指定域访问跨域资源
                .setHeader("Access-Control-Allow-Origin", "*")
                // 允许所有请求方式
                .setHeader("Access-Control-Allow-Methods", "*")
                // 允许的header参数
                .setHeader("Access-Control-Allow-Headers", "*")
                // 有效时间
                .setHeader("Access-Control-Max-Age", "3600")
            // 如果是预检请求，则立即返回到前端
            SaRouter.match(SaHttpMethod.OPTIONS).back();
        }
    }
}
