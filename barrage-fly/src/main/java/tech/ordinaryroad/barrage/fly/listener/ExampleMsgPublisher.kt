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

import cn.hutool.core.thread.ThreadUtil
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import tech.ordinaryroad.barrage.fly.dto.msg.BarrageFlyMsgDTO
import tech.ordinaryroad.barrage.fly.util.BarrageFlyUtil
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Msg示例
 *
 * @author mjz
 * @date 2023/10/11
 */
class ExampleMsgPublisher : Publisher<BarrageFlyMsgDTO>, Subscription {

    private var scheduleAtFixedRate: ScheduledFuture<*>

    private var subscriber: Subscriber<in BarrageFlyMsgDTO>? = null

    init {
        scheduleAtFixedRate = SCHEDULED_EXECUTOR.scheduleAtFixedRate({
            BarrageFlyUtil.generateRandomMsgDTOs().forEach {
                this.subscriber?.onNext(it)
            }
        }, 0, 2, TimeUnit.SECONDS)
    }

    override fun subscribe(subscriber: Subscriber<in BarrageFlyMsgDTO>) {
        subscriber.onSubscribe(this)
        this.subscriber = subscriber
    }

    override fun request(n: Long) {
        // ignore
    }

    override fun cancel() {
        this.subscriber = null
        scheduleAtFixedRate.cancel(true)
    }

    companion object {
        private val SCHEDULED_EXECUTOR = ThreadUtil.createScheduledExecutor(2).apply {
            setThreadFactory {
                Thread(it, "MsgPublisher示例线程")
            }
        }
    }
}