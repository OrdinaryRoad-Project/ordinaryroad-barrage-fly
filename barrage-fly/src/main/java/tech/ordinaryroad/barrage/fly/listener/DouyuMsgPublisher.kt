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
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuBinaryFrameHandler

/**
 * @author mjz
 * @date 2023/9/14
 */
class DouyuMsgPublisher : IDouyuMsgListener, Publisher<IMsg> {

    private var subscriber: Subscriber<in IMsg>? = null

    override fun onMsg(binaryFrameHandler: DouyuBinaryFrameHandler, msg: IMsg) {
        try {
            this.subscriber?.onNext(msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun subscribe(subscriber: Subscriber<in IMsg>) {
        this.subscriber = subscriber
    }

}