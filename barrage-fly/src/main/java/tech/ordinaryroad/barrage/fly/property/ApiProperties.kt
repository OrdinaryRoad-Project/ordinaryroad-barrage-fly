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
package tech.ordinaryroad.barrage.fly.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * @author mjz
 * @date 2024/4/23
 */
@Configuration
@ConfigurationProperties(prefix = "tech.ordinaryroad.barrage.fly.config.api")
data class ApiProperties(
    /**
     * 接口前缀，以`/`开头
     */
    var prefix: String? = null,
)
