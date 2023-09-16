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
import tech.ordinaryroad.live.chat.client.bilibili.constant.BilibiliCmdEnum
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener
import tech.ordinaryroad.live.chat.client.bilibili.msg.DanmuMsgMsg
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendGiftMsg
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendSmsReplyMsg
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg

/**
 * @author mjz
 * @date 2023/9/14
 */
class BilibiliMsgPublisher : IBilibiliMsgListener, Publisher<IMsg>, Subscription {

    private var subscriber: Subscriber<in IMsg>? = null

    override fun onMsg(binaryFrameHandler: BilibiliBinaryFrameHandler, msg: IMsg) {
        // B站防止重复添加消息，因为B站的弹幕消息和礼物消息只是字段属性不同，都是属于SendSmsReplyMsg
        if (msg is SendSmsReplyMsg) {
            if (msg.cmdEnum == BilibiliCmdEnum.DANMU_MSG || msg.cmdEnum == BilibiliCmdEnum.SEND_GIFT) {
                return
            }
        }
        this.subscriber?.onNext(msg)
    }

    override fun onDanmuMsg(t: BilibiliBinaryFrameHandler, msg: DanmuMsgMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onGiftMsg(t: BilibiliBinaryFrameHandler, msg: SendGiftMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun subscribe(subscriber: Subscriber<in IMsg>) {
        subscriber.onSubscribe(this)
        this.subscriber = subscriber
    }

    override fun request(n: Long) {
        // ignored
    }

    override fun cancel() {
        // ignored
    }

}