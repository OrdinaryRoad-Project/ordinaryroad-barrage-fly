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

package tech.ordinaryroad.barrage.fly

import cn.hutool.core.util.StrUtil
import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.support.PropertiesLoaderUtils
import tech.ordinaryroad.barrage.fly.context.BarrageFlyTaskContext
import tech.ordinaryroad.commons.core.config.PasswordEncoderConfig
import tech.ordinaryroad.commons.core.config.RedisTemplateConfiguration
import tech.ordinaryroad.commons.core.service.RedisService
import tk.mybatis.spring.annotation.MapperScan
import javax.annotation.PreDestroy

@EnableAdminServer
@SpringBootApplication(
    exclude = [PasswordEncoderConfig::class, RedisTemplateConfiguration::class, RedisService::class],
)
@MapperScan("tech.ordinaryroad.barrage.fly.dal.dao")
class OrdinaryroadBarrageFlyApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<OrdinaryroadBarrageFlyApplication>(*args)
            val properties = PropertiesLoaderUtils.loadAllProperties("properties/generated/version.properties")
            println(StrUtil.format("LiveChatClient Version: {}", properties.getProperty("liveChatClientVersion")))
        }
    }

    @PreDestroy
    fun preDestroy() {
        BarrageFlyTaskContext.destroy()
    }

}


