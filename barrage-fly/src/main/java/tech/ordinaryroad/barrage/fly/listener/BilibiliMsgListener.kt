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

package tech.ordinaryroad.barrage.fly.listener

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum
import tech.ordinaryroad.barrage.fly.context.BarrageFlyTaskContext
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener
import tech.ordinaryroad.live.chat.client.bilibili.msg.DanmuMsgMsg
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendGiftMsg
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg

/**
 *
 *
 * @author mjz
 * @date 2023/9/10
 */
@Component
class BilibiliMsgListener : IBilibiliMsgListener {

    private val log = LoggerFactory.getLogger(BilibiliMsgListener::class.java)

    override fun onMsg(binaryFrameHandler: BilibiliBinaryFrameHandler, msg: IMsg) {
        // B站防止重复添加消息，因为B站的弹幕消息和礼物消息只是字段属性不同，都是属于SendSmsReplyMsg
//        if (msg is SendSmsReplyMsg) {
//            if (msg.cmdEnum == BilibiliCmdEnum.DANMU_MSG || msg.cmdEnum == BilibiliCmdEnum.SEND_GIFT) {
//                return
//            }
//        }

//        BarrageFlyTaskContext.getContexts(PlatformEnum.bilibili, binaryFrameHandler.roomId.toString()).forEach {
//            it.publishMsg(PlatformEnum.bilibili, binaryFrameHandler.roomId.toString(), msg)
//        }
    }

    override fun onDanmuMsg(binaryFrameHandler: BilibiliBinaryFrameHandler, msg: DanmuMsgMsg) {
        BarrageFlyTaskContext.getContexts(PlatformEnum.bilibili, binaryFrameHandler.roomId.toString()).forEach {
            it.publishMsg(PlatformEnum.bilibili, binaryFrameHandler.roomId.toString(), msg)
        }
    }

    override fun onGiftMsg(binaryFrameHandler: BilibiliBinaryFrameHandler, msg: SendGiftMsg) {
        BarrageFlyTaskContext.getContexts(PlatformEnum.bilibili, binaryFrameHandler.roomId.toString()).forEach {
            it.publishMsg(PlatformEnum.bilibili, binaryFrameHandler.roomId.toString(), msg)
        }
    }

}