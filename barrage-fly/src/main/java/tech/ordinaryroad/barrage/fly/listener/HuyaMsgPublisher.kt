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
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaCmdEnum
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaMsgListener
import tech.ordinaryroad.live.chat.client.huya.msg.MessageNoticeMsg
import tech.ordinaryroad.live.chat.client.huya.msg.PushMessage
import tech.ordinaryroad.live.chat.client.huya.msg.SendItemSubBroadcastPacketMsg
import tech.ordinaryroad.live.chat.client.huya.msg.dto.MsgItem
import tech.ordinaryroad.live.chat.client.huya.netty.handler.HuyaBinaryFrameHandler

/**
 * @author mjz
 * @date 2023/10/3
 */
class HuyaMsgPublisher : IHuyaMsgListener, Publisher<IMsg>, Subscription {

    private var subscriber: Subscriber<in IMsg>? = null

    override fun onMsg(binaryFrameHandler: HuyaBinaryFrameHandler, msg: IMsg) {
        // 防止重复添加消息，因为弹幕消息和礼物消息只是字段属性不同，都是属于PushMessage、MsgItem
        if (msg is PushMessage || msg is MsgItem) {
            val cmdMsg = msg as ICmdMsg<*>
            if (cmdMsg.cmdEnum == HuyaCmdEnum.MessageNotice || cmdMsg.cmdEnum == HuyaCmdEnum.SendItemSubBroadcastPacket) {
                return
            }
        }
        this.subscriber?.onNext(msg)
    }

    override fun onDanmuMsg(t: HuyaBinaryFrameHandler, msg: MessageNoticeMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onGiftMsg(t: HuyaBinaryFrameHandler, msg: SendItemSubBroadcastPacketMsg) {
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