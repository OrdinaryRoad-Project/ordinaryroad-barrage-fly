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
import cn.hutool.core.io.resource.ResourceUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.extra.spring.SpringUtil
import tech.ordinaryroad.barrage.fly.constant.MsgTypeEnum
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO
import tech.ordinaryroad.barrage.fly.dto.msg.BarrageFlyMsgDTO
import tech.ordinaryroad.barrage.fly.express.BarrageFlyExpressContext
import tech.ordinaryroad.barrage.fly.express.BarrageFlyExpressRunner
import tech.ordinaryroad.barrage.fly.express.operator.OperatorSendDanmu.Companion.KEY_DO_SEND_DANMU_BOOLEAN
import tech.ordinaryroad.live.chat.client.bilibili.msg.DanmuMsgMsg
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendGiftMsg
import tech.ordinaryroad.live.chat.client.bilibili.msg.SuperChatMessageMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg.OBJECT_MAPPER
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil
import tech.ordinaryroad.live.chat.client.douyu.msg.ChatmsgMsg
import tech.ordinaryroad.live.chat.client.douyu.msg.DgbMsg
import tech.ordinaryroad.live.chat.client.huya.msg.MessageNoticeMsg
import tech.ordinaryroad.live.chat.client.huya.msg.SendItemSubBroadcastPacketMsg
import java.util.function.Consumer

/**
 *
 *
 * @author mjz
 * @date 2023/9/15
 */
object BarrageFlyUtil {

    private val msgFiles = arrayOf(
        "express/msg-examples-bilibili.txt",
        "express/msg-examples-bilibili-SUPER_CHAT.txt",
        "express/msg-examples-douyu.txt",
        "express/msg-examples-huya.txt"
    )

    fun generateRandomMsgDTOs(count: Int = 10, consumer: Consumer<BarrageFlyMsgDTO>) {
        var current = 0
        while (current < count) {
            for (i in msgFiles.indices) {
                val msgFile = msgFiles.random()
                val lines = FileUtil.readUtf8Lines(ResourceUtil.getResource(msgFile))
                for (j in 0 until lines.size) {
                    val line = lines.random()
                    if (current >= count) {
                        return
                    }

                    var barrageFlyMsgDTO: BarrageFlyMsgDTO? = null
                    try {
                        val jsonNode = OBJECT_MAPPER.readTree(line)
                        val roomId = jsonNode.get("roomId").asText()
                        val msgString = jsonNode.get("msg").toString()
                        val platformEnum = PlatformEnum.getByString(jsonNode.get("platform").asText())
                        val msgTypeEnum = MsgTypeEnum.getByString(jsonNode.get("type").asText())
                        val msg: IMsg? = if (platformEnum == null || msgTypeEnum == null) null
                        else when (platformEnum) {
                            PlatformEnum.BILIBILI -> {
                                when (msgTypeEnum) {
                                    MsgTypeEnum.DANMU -> OBJECT_MAPPER.readValue(
                                        msgString,
                                        DanmuMsgMsg::class.java
                                    )

                                    MsgTypeEnum.GIFT -> OBJECT_MAPPER.readValue(
                                        msgString,
                                        SendGiftMsg::class.java
                                    )

                                    MsgTypeEnum.SUPER_CHAT -> OBJECT_MAPPER.readValue(
                                        msgString,
                                        SuperChatMessageMsg::class.java
                                    )

                                    else -> null
                                }
                            }

                            PlatformEnum.DOUYU -> {
                                when (msgTypeEnum) {
                                    MsgTypeEnum.DANMU -> OBJECT_MAPPER.readValue(
                                        msgString,
                                        ChatmsgMsg::class.java
                                    )

                                    MsgTypeEnum.GIFT -> OBJECT_MAPPER.readValue(
                                        msgString,
                                        DgbMsg::class.java
                                    )

                                    MsgTypeEnum.SUPER_CHAT -> {
                                        null
                                    }

                                    else -> null
                                }
                            }

                            PlatformEnum.HUYA -> {
                                when (msgTypeEnum) {
                                    MsgTypeEnum.DANMU -> OBJECT_MAPPER.readValue(
                                        msgString,
                                        MessageNoticeMsg::class.java
                                    )

                                    MsgTypeEnum.GIFT -> {
                                        OBJECT_MAPPER.readValue(
                                            msgString,
                                            SendItemSubBroadcastPacketMsg::class.java
                                        )
                                    }

                                    MsgTypeEnum.SUPER_CHAT -> {
                                        null
                                    }

                                    else -> null
                                }
                            }

                            else -> null
                        }
                        if (msg != null) {
                            barrageFlyMsgDTO = BarrageFlyMsgDTO(roomId, msg)
                        }
                    } catch (e: Exception) {
                        // ignore
                    }
                    if (barrageFlyMsgDTO != null && RandomUtil.randomBoolean()) {
                        current++
                        consumer.accept(barrageFlyMsgDTO)
                    }
                }
            }
        }
    }

    fun generateRandomMsgDTOs(count: Int = 10): List<BarrageFlyMsgDTO> {
        val list = ArrayList<BarrageFlyMsgDTO>(count)
        generateRandomMsgDTOs(count, list::add)
        while (list.size > count) {
            list.removeAt(list.size - 1)
        }
        return list
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
            val expressRunner = SpringUtil.getBean(BarrageFlyExpressRunner::class.java)
            val context = BarrageFlyExpressContext()
            context[KEY_DO_SEND_DANMU_BOOLEAN] = false
            generateRandomMsgDTOs().forEach { msgDTO ->
                context.setMsg(msgDTO)
                var result = expressRunner.executePreMapExpress(this.msgPreMapExpress, context)
                context.setMsg(result)
                if (expressRunner.executeFilterExpress(this.msgFilterExpress, context)) {
                    result = expressRunner.executePostMapExpress(this.msgPostMapExpress, context)
                    context.setMsg(result)
                }
            }
        } catch (e: Exception) {
            throw e
        }
        return true
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val generateRandomMsgDTOs = generateRandomMsgDTOs(10)
        println(generateRandomMsgDTOs.size)
        for (msg in generateRandomMsgDTOs) {
            println(msg)
        }
    }

}