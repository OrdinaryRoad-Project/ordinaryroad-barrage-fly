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
package tech.ordinaryroad.barrage.fly.context

import cn.hutool.cache.impl.CacheObj
import cn.hutool.cache.impl.TimedCache
import cn.hutool.core.lang.mutable.Mutable
import cn.hutool.core.util.StrUtil
import com.fasterxml.jackson.databind.node.ObjectNode
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.messaging.rsocket.RSocketRequester
import tech.ordinaryroad.barrage.fly.constant.BarrageFlyTaskStatusEnum
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO
import tech.ordinaryroad.barrage.fly.listener.*
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.commons.client.BaseLiveChatClient
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums
import tech.ordinaryroad.live.chat.client.commons.client.listener.IClientStatusChangeListener
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil
import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient
import tech.ordinaryroad.live.chat.client.douyin.config.DouyinLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener
import tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClient
import tech.ordinaryroad.live.chat.client.huya.config.HuyaLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaMsgListener
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener
import tech.ordinaryroad.live.chat.client.servers.netty.client.config.BaseNettyClientConfig
import java.beans.PropertyChangeSupport
import java.util.concurrent.ConcurrentHashMap

/**
 * 每个任务的上下文
 *
 * @author mjz
 * @date 2023/9/10
 */

@Suppress("UNCHECKED_CAST")
class BarrageFlyTaskContext(
    /**
     * taskId不可改变，其他改变需要重启任务
     */
    val taskId: String,
    var barrageFlyTaskDO: BarrageFlyTaskDO,
    var clientConfigJson: String
) {

    /**
     * 一个任务多个客户端，每个客户端对应一个Channel
     */
    val rSocketClientMsgPublishers = ConcurrentHashMap<Int, Publisher<IMsg>>()
    private val log = LoggerFactory.getLogger(BarrageFlyTaskContext::class.java)

    private var client: BaseLiveChatClient<*, *, *>? = null

    var status = BarrageFlyTaskStatusEnum.NEW
        set(status) {
            val oldStatus = field
            field = status
            statusChangeSupport.firePropertyChange("status", oldStatus, status)
        }
    var statusChangeSupport = PropertyChangeSupport(status)

    private val clientStatusChangeListener = IClientStatusChangeListener { _, _, newValue ->
        this.status = when (newValue) {
            ClientStatusEnums.CONNECTING -> {
                BarrageFlyTaskStatusEnum.CONNECTING
            }

            ClientStatusEnums.RECONNECTING -> {
                BarrageFlyTaskStatusEnum.RECONNECTING
            }

            ClientStatusEnums.CONNECTED -> {
                BarrageFlyTaskStatusEnum.RUNNING
            }

            ClientStatusEnums.CONNECT_FAILED -> {
                BarrageFlyTaskStatusEnum.CONNECT_FAILED
            }

            ClientStatusEnums.DISCONNECTED -> {
                BarrageFlyTaskStatusEnum.DISCONNECTED
            }

            else -> {
                BarrageFlyTaskStatusEnum.STOPPED
            }
        }
    }

    fun destroy() {
        if (log.isDebugEnabled) {
            log.debug("destroy context")
        }
        rSocketClientMsgPublishers.clear()
        client?.let {
            it.destroy()
            client = null
        }
        status = BarrageFlyTaskStatusEnum.STOPPED
    }

    fun start() {
        if (log.isDebugEnabled) {
            log.debug("start client")
        }
        (client ?: createClient(barrageFlyTaskDO.platform, clientConfigJson)).let {
            it.addStatusChangeListener(clientStatusChangeListener)
            it.connect()
            this.client = it
        }
    }

    private fun <Config : BaseNettyClientConfig> createClientConfig(
        platform: PlatformEnum,
        clientConfigJson: String
    ): Config {
        return when (platform) {
            PlatformEnum.BILIBILI -> {
                OrJacksonUtil.getInstance()
                    .readValue(clientConfigJson, BilibiliLiveChatClientConfig::class.java) as Config
            }

            PlatformEnum.DOUYU -> {
                OrJacksonUtil.getInstance().readValue(clientConfigJson, DouyuLiveChatClientConfig::class.java) as Config
            }

            PlatformEnum.HUYA -> {
                OrJacksonUtil.getInstance().readValue(clientConfigJson, HuyaLiveChatClientConfig::class.java) as Config
            }

            PlatformEnum.DOUYIN -> {
                OrJacksonUtil.getInstance()
                    .readValue(clientConfigJson, DouyinLiveChatClientConfig::class.java) as Config
            }

            PlatformEnum.KUAISHOU -> {
                OrJacksonUtil.getInstance()
                    .readValue(clientConfigJson, KuaishouLiveChatClientConfig::class.java) as Config
            }
        }
    }

    private fun <MsgListener : Publisher<IMsg>> createMsgListener(platform: PlatformEnum): MsgListener {
        return when (platform) {
            PlatformEnum.BILIBILI -> {
                BilibiliMsgPublisher() as MsgListener
            }

            PlatformEnum.DOUYU -> {
                DouyuMsgPublisher() as MsgListener
            }

            PlatformEnum.HUYA -> {
                HuyaMsgPublisher() as MsgListener
            }

            PlatformEnum.DOUYIN -> {
                DouyinMsgPublisher() as MsgListener
            }

            PlatformEnum.KUAISHOU -> {
                KuaishouMsgPublisher() as MsgListener
            }
        }
    }

    private fun <Client : BaseLiveChatClient<*, *, *>> createClient(
        platform: PlatformEnum, clientConfigJson: String
    ): Client {
        return when (platform) {
            PlatformEnum.BILIBILI -> BilibiliLiveChatClient(createClientConfig(platform, clientConfigJson))
            PlatformEnum.DOUYU -> DouyuLiveChatClient(createClientConfig(platform, clientConfigJson))
            PlatformEnum.HUYA -> HuyaLiveChatClient(createClientConfig(platform, clientConfigJson))
            PlatformEnum.DOUYIN -> DouyinLiveChatClient(createClientConfig(platform, clientConfigJson))
            PlatformEnum.KUAISHOU -> KuaishouLiveChatClient(createClientConfig(platform, clientConfigJson))
        } as Client
    }

    /**
     * 每个Channel对应一个消息队列
     */
    fun registerChannel(requester: RSocketRequester) {
        if (this.client == null) {
            return
        }

        val hashCode = requester.hashCode()
        val msgListener: Publisher<IMsg>
        when (val platform = barrageFlyTaskDO.platform) {
            PlatformEnum.BILIBILI -> {
                msgListener = createMsgListener<BilibiliMsgPublisher>(platform)
                (this.client as BilibiliLiveChatClient).addMsgListener(msgListener)
            }

            PlatformEnum.DOUYU -> {
                msgListener = createMsgListener<DouyuMsgPublisher>(platform)
                (this.client as DouyuLiveChatClient).addMsgListener(msgListener)
            }

            PlatformEnum.HUYA -> {
                msgListener = createMsgListener<HuyaMsgPublisher>(platform)
                (this.client as HuyaLiveChatClient).addMsgListener(msgListener)
            }

            PlatformEnum.DOUYIN -> {
                msgListener = createMsgListener<DouyinMsgPublisher>(platform)
                (this.client as DouyinLiveChatClient).addMsgListener(msgListener)
            }

            PlatformEnum.KUAISHOU -> {
                msgListener = createMsgListener<KuaishouMsgPublisher>(platform)
                (this.client as KuaishouLiveChatClient).addMsgListener(msgListener)
            }
        }
        this.rSocketClientMsgPublishers[hashCode] = msgListener
    }

    fun unregisterChannel(requester: RSocketRequester) {
        if (this.client == null) {
            return
        }

        val hashCode = requester.hashCode()
        val publisher = this.rSocketClientMsgPublishers.remove(hashCode) ?: return
        when (barrageFlyTaskDO.platform) {
            PlatformEnum.BILIBILI -> {
                (this.client as BilibiliLiveChatClient).removeMsgListener(publisher as IBilibiliMsgListener)
            }

            PlatformEnum.DOUYU -> {
                (this.client as DouyuLiveChatClient).removeMsgListener(publisher as IDouyuMsgListener)
            }

            PlatformEnum.HUYA -> {
                (this.client as HuyaLiveChatClient).removeMsgListener(publisher as IHuyaMsgListener)
            }

            PlatformEnum.DOUYIN -> {
                (this.client as DouyinLiveChatClient).removeMsgListener(publisher as IDouyinMsgListener)
            }

            PlatformEnum.KUAISHOU -> {
                (this.client as KuaishouLiveChatClient).removeMsgListener(publisher as IKuaishouMsgListener)
            }

            else -> {
                // ignored
            }
        }
    }

    fun sendDanmu(danmu: String) {
        client!!.sendDanmu(danmu)
    }

    companion object {
        val connectedClients = TimedCache(
            24 * 3600 * 1000L, ConcurrentHashMap<Mutable<Int>, CacheObj<Int, RSocketRequester>>()
        )

        /**
         * <TaskId,BarrageFlyTaskContext>
         */
        val taskContexts = ConcurrentHashMap<String, BarrageFlyTaskContext>()

        /**
         * 任务启动时注册上下文，未注册时新建
         */
        fun getOrCreateContext(barrageFlyTaskDO: BarrageFlyTaskDO): BarrageFlyTaskContext {
            val clientConfigJson = (OrJacksonUtil.getInstance().readTree(
                StrUtil.blankToDefault(barrageFlyTaskDO.platformConfigJson, StrUtil.EMPTY_JSON)
            ) as ObjectNode).apply {
                put("roomId", barrageFlyTaskDO.roomId)
                barrageFlyTaskDO.cookie?.let { put("cookie", it) }
                barrageFlyTaskDO.socks5ProxyHost?.let { put("socks5ProxyHost", it) }
                barrageFlyTaskDO.socks5ProxyPort?.let { put("socks5ProxyPort", it) }
                barrageFlyTaskDO.socks5ProxyUsername?.let { put("socks5ProxyUsername", it) }
                barrageFlyTaskDO.socks5ProxyPassword?.let { put("socks5ProxyPassword", it) }
            }.toString()
            return taskContexts.getOrPut(barrageFlyTaskDO.uuid) {
                BarrageFlyTaskContext(
                    barrageFlyTaskDO.uuid,
                    barrageFlyTaskDO,
                    clientConfigJson
                )
            }.apply {
                this.barrageFlyTaskDO = barrageFlyTaskDO
                this.clientConfigJson = clientConfigJson
            }
        }

        /**
         * 任务终止时移除上下文
         */
        fun removeContext(taskId: String) {
            taskContexts.remove(taskId)?.destroy()
        }

        fun getContext(taskId: String?): BarrageFlyTaskContext? {
            if (taskId == null) {
                return null
            }
            return taskContexts[taskId]
        }

        fun getContexts(taskId: List<String>): List<BarrageFlyTaskContext> {
            if (taskId.isEmpty()) {
                return emptyList()
            }
            return taskContexts.filterKeys {
                taskId.contains(it)
            }.map {
                it.value
            }
        }

        fun getContexts(platform: PlatformEnum, roomId: String): List<BarrageFlyTaskContext> {
            return taskContexts.filterValues { it.barrageFlyTaskDO.platform == platform && it.barrageFlyTaskDO.roomId == roomId }
                .map { it.value }
        }

        fun destroy() {
            taskContexts.forEach { (_, v) -> v.destroy() }
        }

        fun getTaskStatus(taskId: String): BarrageFlyTaskStatusEnum {
            val context = getContext(taskId) ?: return BarrageFlyTaskStatusEnum.STOPPED
            return context.status
        }

        /**
         * 保存新连接的客户端
         */
        fun registerClient(requester: RSocketRequester) {
            val hashCode = requester.hashCode()
            connectedClients.put(hashCode, requester)
        }

        /**
         * 注销客户端
         */
        fun unregisterClient(requester: RSocketRequester) {
            val hashCode = requester.hashCode()
            connectedClients.remove(hashCode)
        }

        /**
         * 注册Channel
         */
        fun registerChannels(taskIds: List<String>, requester: RSocketRequester) {
            getContexts(taskIds).forEach {
                it.registerChannel(requester)
            }
        }

        /**
         * 关闭指定的Channel
         */
        fun unregisterChannels(taskIds: List<String>, requester: RSocketRequester) {
            getContexts(taskIds).forEach {
                it.unregisterChannel(requester)
            }
        }

        /**
         * 关闭所有Channel
         */
        fun unregisterAllChannels(requester: RSocketRequester) {
            taskContexts.forEach { (_, v) ->
                v.unregisterChannel(requester)
            }
        }
    }
}
