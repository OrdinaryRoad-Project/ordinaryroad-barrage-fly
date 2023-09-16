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

package tech.ordinaryroad.barrage.fly.util

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.EnumUtil
import cn.hutool.core.util.RandomUtil
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO
import tech.ordinaryroad.barrage.fly.dto.msg.BarrageFlyMsgDTO
import tech.ordinaryroad.barrage.fly.express.BarrageFlyExpressContext
import tech.ordinaryroad.barrage.fly.express.BarrageFlyExpressRunner
import tech.ordinaryroad.live.chat.client.bilibili.constant.OperationEnum
import tech.ordinaryroad.live.chat.client.bilibili.util.BilibiliCodecUtil
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg.OBJECT_MAPPER
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil
import tech.ordinaryroad.live.chat.client.douyu.util.DouyuCodecUtil
import java.nio.charset.StandardCharsets

/**
 *
 *
 * @author mjz
 * @date 2023/9/15
 */
object BarrageFlyUtil {

    fun generateRandomMsgDTOs(): List<BarrageFlyMsgDTO> {
        return FileUtil.readLines("express/msg-examples.txt", StandardCharsets.UTF_8).map {
            it ?: return@map null
            var barrageFlyMsgDTO: BarrageFlyMsgDTO? = null
            try {
                val jsonNode = OBJECT_MAPPER.readTree(it)
                val platform = jsonNode.get("platform").asText()
                val roomId = jsonNode.get("roomId").asText()
                val msgString = jsonNode.get("msg").asText()
                val platformEnum = EnumUtil.fromString(PlatformEnum::class.java, platform) ?: return@map null
                val msg = when (platformEnum) {
                    PlatformEnum.BILIBILI -> BilibiliCodecUtil.parse(OperationEnum.SEND_SMS_REPLY, msgString).get()
                    PlatformEnum.DOUYU -> DouyuCodecUtil.parseDouyuSttString(msgString, DouyuCodecUtil.MSG_TYPE_RECEIVE)
                }!!
                barrageFlyMsgDTO = BarrageFlyMsgDTO(roomId, msg)
            } catch (e: Exception) {
                // ignore
            }
            if (RandomUtil.randomBoolean()) barrageFlyMsgDTO else null
        }.filterNotNull()
    }

    fun BarrageFlyTaskDO.validate(): Boolean {
        if (platform == null) {
            return false
        }
        if (roomId.isNullOrBlank()) {
            return false
        }
        try {
            OrLiveChatCookieUtil.parseCookieString(cookie)
        } catch (e: Exception) {
            throw e
        }
        return true
    }

    fun BarrageFlyTaskDO.validateTaskExpress(): Boolean {
        try {
            var context = BarrageFlyExpressContext()
            generateRandomMsgDTOs()
                .map {
                    context.setMsg(it)
                    val result = BarrageFlyExpressRunner.executePreMapExpress(this.msgPreMapExpress, context)
                    context.setMsg(result)
                    result
                }
                .filter {
                    BarrageFlyExpressRunner.executeFilterExpress(this.msgFilterExpress, context)
                }
                .map {
                    val result = BarrageFlyExpressRunner.executePostMapExpress(this.msgPostMapExpress, context)
                    context.setMsg(result)
                    result
                }
        } catch (e: Exception) {
            throw e
        }
        return true
    }

}