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

import cn.dev33.satoken.reactor.filter.SaReactorFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * [Sa-Token 权限认证] 全局配置类
 */
@Configuration
class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    fun saReactorFilter(): SaReactorFilter = SaReactorFilter()
}
