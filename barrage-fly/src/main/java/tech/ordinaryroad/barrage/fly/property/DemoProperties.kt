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

import cn.hutool.core.util.NumberUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.cron.CronUtil
import cn.hutool.cron.pattern.CronPatternBuilder
import cn.hutool.cron.pattern.Part
import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import tech.ordinaryroad.barrage.fly.context.BarrageFlyTaskContext
import javax.annotation.PostConstruct

/**
 *
 *
 * @author mjz
 * @date 2024/5/16
 */
@Configuration
@ConfigurationProperties(prefix = "tech.ordinaryroad.barrage.fly.config.demo")
data class DemoProperties(
    var stopTaskEnable: Boolean = true,
    var stopTaskTime: String = "00:00:00"
) {
    private val log = LoggerFactory.getLogger(DemoProperties::class.java)

    @PostConstruct
    fun postConstruct() {
        if (StrUtil.equalsIgnoreCase(SpringUtil.getActiveProfile(), "demo")) {
            log.warn("当前为演示DEMO环境")

            if (stopTaskEnable) {
                log.warn("每天${stopTaskTime}将停止所有任务")
                val split = stopTaskTime.split(":")
                CronUtil.schedule(
                    CronPatternBuilder.of()
                        .setValues(Part.HOUR, NumberUtil.parseInt(split[0]))
                        .setValues(Part.MINUTE, NumberUtil.parseInt(split[1]))
                        .setValues(Part.SECOND, NumberUtil.parseInt(split[2]))
                        .build(), Runnable {
                        log.info("停止所有任务开始")
                        BarrageFlyTaskContext.destroy()
                        log.info("停止所有任务结束")
                    }
                )
                CronUtil.setMatchSecond(true)
                CronUtil.start()
            }
        }
    }
}