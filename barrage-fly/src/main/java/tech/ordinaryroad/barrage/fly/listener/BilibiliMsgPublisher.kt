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

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliCmdEnum
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.*
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg

/**
 * @author mjz
 * @date 2023/9/14
 */
class BilibiliMsgPublisher : IBilibiliMsgListener, Publisher<IMsg>, Subscription {

    private var subscriber: Subscriber<in IMsg>? = null

    override fun onMsg(binaryFrameHandler: BilibiliBinaryFrameHandler, msg: IMsg) {
        // B站防止重复添加消息，因为B站的弹幕、礼物、醒目留言、入房消息只是字段属性不同，都是属于SendSmsReplyMsg
        if (msg is MessageMsg) {
            if (msg.cmdEnum == BilibiliCmdEnum.DANMU_MSG || msg.cmdEnum == BilibiliCmdEnum.SEND_GIFT
                || msg.cmdEnum == BilibiliCmdEnum.SUPER_CHAT_MESSAGE || msg.cmdEnum == BilibiliCmdEnum.INTERACT_WORD
                || msg.cmdEnum == BilibiliCmdEnum.LIKE_INFO_V3_UPDATE
                || msg.cmdEnum == BilibiliCmdEnum.LIVE || msg.cmdEnum == BilibiliCmdEnum.STOP_LIVE_ROOM_LIST
            ) {
                return
            }
        }
        this.subscriber?.onNext(msg)
    }

    override fun onDanmuMsg(t: BilibiliBinaryFrameHandler, msg: DanmuMsgMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onGiftMsg(t: BilibiliBinaryFrameHandler, msg: SendGiftMsg) {
        if (msg.giftCount <= 0) {
            return
        }
        this.subscriber?.onNext(msg)
    }

    override fun onSuperChatMsg(t: BilibiliBinaryFrameHandler, msg: SuperChatMessageMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onEnterRoomMsg(t: BilibiliBinaryFrameHandler, msg: InteractWordMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onLikeMsg(t: BilibiliBinaryFrameHandler, msg: LikeInfoV3ClickMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onLiveStatusMsg(t: BilibiliBinaryFrameHandler?, msg: BilibiliLiveStatusChangeMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun subscribe(subscriber: Subscriber<in IMsg>) {
        subscriber.onSubscribe(this)
        this.subscriber = subscriber
    }

    override fun request(n: Long) {
        // ignore
    }

    override fun cancel() {
        this.subscriber = null
    }

}