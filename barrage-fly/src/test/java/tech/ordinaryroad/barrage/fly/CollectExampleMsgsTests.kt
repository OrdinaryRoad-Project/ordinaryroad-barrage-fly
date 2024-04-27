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

package tech.ordinaryroad.barrage.fly

import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import org.junit.jupiter.api.Test
import tech.ordinaryroad.barrage.fly.dto.msg.BarrageFlyMsgDTO
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.DanmuMsgMsg
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.SendGiftMsg
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.SuperChatMessageMsg
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.ChatmsgMsg
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.DgbMsg
import tech.ordinaryroad.live.chat.client.codec.huya.msg.MessageNoticeMsg
import tech.ordinaryroad.live.chat.client.codec.huya.msg.SendItemSubBroadcastPacketMsg
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener
import tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClient
import tech.ordinaryroad.live.chat.client.huya.config.HuyaLiveChatClientConfig
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaMsgListener

/**
 * @author mjz
 * @date 2023/10/11
 */
class CollectExampleMsgsTests {

    private val maxMsg = 50

    @Volatile
    var cntBilibili = 0

    @Test
    fun collectBilibiliExampleMsgs() {
        val roomId = 7777L
        val file = FileUtil.file("msg-examples-bilibili-${DateUtil.current()}.txt")
        val bilibiliLiveChatClient = BilibiliLiveChatClient(BilibiliLiveChatClientConfig().apply {
            setRoomId(roomId)
        }, object : IBilibiliMsgListener {
            override fun onDanmuMsg(msg: DanmuMsgMsg) {
                if (cntBilibili < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntBilibili++
                }
            }

            override fun onGiftMsg(msg: SendGiftMsg) {
                if (cntBilibili < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntBilibili++
                }
            }

            override fun onSuperChatMsg(msg: SuperChatMessageMsg) {
                if (cntBilibili < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntBilibili++
                }
            }
        })
        bilibiliLiveChatClient.connect()

        while (cntBilibili < maxMsg) {
            // ignore
        }

        println(file.path)
        bilibiliLiveChatClient.destroy()
    }

    @Volatile
    var cntDouyu = 0

    @Test
    fun collectDouyuExampleMsgs() {
        val roomId = 252140L
        val file = FileUtil.file("msg-examples-douyu-${DateUtil.current()}.txt")
        val douyuLiveChatClient = DouyuLiveChatClient(DouyuLiveChatClientConfig().apply {
            setRoomId(roomId)
        }, object : IDouyuMsgListener {
            override fun onDanmuMsg(msg: ChatmsgMsg) {
                if (cntDouyu < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntDouyu++
                }
            }

            override fun onGiftMsg(msg: DgbMsg) {
                if (cntDouyu < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntDouyu++
                }
            }
        })
        douyuLiveChatClient.connect()

        while (cntDouyu < maxMsg) {
            // ignore
        }

        println(file.path)
        douyuLiveChatClient.destroy()
    }

    @Volatile
    var cntHuya = 0

    @Test
    fun collectHuyaExampleMsgs() {
        val roomId = 522222L
        val file = FileUtil.file("msg-examples-huya-${DateUtil.current()}.txt")
        val huyaLiveChatClient = HuyaLiveChatClient(HuyaLiveChatClientConfig().apply {
            setRoomId(roomId)
        }, object : IHuyaMsgListener {
            override fun onDanmuMsg(msg: MessageNoticeMsg) {
                if (cntHuya < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntHuya++
                }
            }

            override fun onGiftMsg(msg: SendItemSubBroadcastPacketMsg) {
                if (cntHuya < maxMsg) {
                    FileUtil.appendUtf8Lines(arrayListOf(BarrageFlyMsgDTO(roomId.toString(), msg).toString()), file)
                    cntHuya++
                }
            }
        })
        huyaLiveChatClient.connect()

        while (cntHuya < maxMsg) {
            // ignore
        }

        println(file.path)
        huyaLiveChatClient.destroy()
    }
}