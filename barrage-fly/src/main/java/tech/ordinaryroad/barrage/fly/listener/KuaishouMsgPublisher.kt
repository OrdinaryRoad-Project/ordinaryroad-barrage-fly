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
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouDanmuMsg
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouGiftMsg
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouLikeMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler

/**
 * @author mjz
 * @date 2024/1/8
 */
class KuaishouMsgPublisher : IKuaishouMsgListener, Publisher<IMsg>, Subscription {

    private var subscriber: Subscriber<in IMsg>? = null

    override fun onMsg(binaryFrameHandler: KuaishouBinaryFrameHandler, msg: IMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onDanmuMsg(t: KuaishouBinaryFrameHandler, msg: KuaishouDanmuMsg) {
        this.subscriber?.onNext(msg)
    }

    override fun onGiftMsg(t: KuaishouBinaryFrameHandler, msg: KuaishouGiftMsg) {
        if (msg.giftCount <= 0) {
            return
        }
        this.subscriber?.onNext(msg)
    }

    override fun onLikeMsg(t: KuaishouBinaryFrameHandler, msg: KuaishouLikeMsg) {
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