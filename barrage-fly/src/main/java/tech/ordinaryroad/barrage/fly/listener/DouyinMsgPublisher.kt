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
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinCmdEnum
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.*
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener
import tech.ordinaryroad.live.chat.client.douyin.netty.handler.DouyinBinaryFrameHandler

/**
 * @author mjz
 * @date 2024/1/8
 */
class DouyinMsgPublisher : IDouyinMsgListener, Publisher<IMsg>, Subscription {

    private var subscriber: Subscriber<in IMsg>? = null

    override fun onMsg(binaryFrameHandler: DouyinBinaryFrameHandler, msg: IMsg) {
        // 防止重复添加消息
        if (msg is DouyinCmdMsg) {
            val cmdMsg = msg as ICmdMsg<*>
            if (cmdMsg.cmdEnum == DouyinCmdEnum.WebcastChatMessage || cmdMsg.cmdEnum == DouyinCmdEnum.WebcastGiftMessage
                || cmdMsg.cmdEnum == DouyinCmdEnum.WebcastMemberMessage || cmdMsg.cmdEnum == DouyinCmdEnum.WebcastLikeMessage
                || cmdMsg.cmdEnum == DouyinCmdEnum.WebcastControlMessage
            ) {
                return
            }
        }
        this.subscriber?.onNext(msg)
    }

    override fun onDanmuMsg(t: DouyinBinaryFrameHandler, msg: DouyinDanmuMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onGiftMsg(t: DouyinBinaryFrameHandler, msg: DouyinGiftMsg) {
        if (msg.giftCount <= 0) {
            return
        }
        this.subscriber?.onNext(msg)
    }

    override fun onEnterRoomMsg(t: DouyinBinaryFrameHandler, msg: DouyinEnterRoomMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onLikeMsg(t: DouyinBinaryFrameHandler, msg: DouyinLikeMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onLiveStatusMsg(t: DouyinBinaryFrameHandler?, msg: DouyinControlMsg) {
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