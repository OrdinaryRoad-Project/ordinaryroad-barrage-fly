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
package tech.ordinaryroad.barrage.fly.constant

import cn.hutool.core.util.StrUtil
import tech.ordinaryroad.live.chat.client.commons.base.msg.*

/**
 * @author mjz
 * @date 2023/9/14
 */
enum class MsgTypeEnum {
    DANMU,
    GIFT,
    SUPER_CHAT,
    ENTER_ROOM,
    LIKE,
    LIVE_STATUS_CHANGE,
    ROOM_STATS,
    SOCIAL,
    ;

    companion object {
        fun getByMsg(msg: IMsg?): MsgTypeEnum? {
            return when (msg) {
                is ISuperChatMsg -> {
                    SUPER_CHAT
                }

                is IDanmuMsg -> {
                    DANMU
                }

                is IGiftMsg -> {
                    GIFT
                }

                is IEnterRoomMsg -> {
                    ENTER_ROOM
                }

                is ILikeMsg -> {
                    LIKE
                }

                is ILiveStatusChangeMsg -> {
                    LIVE_STATUS_CHANGE
                }

                is IRoomStatsMsg -> {
                    ROOM_STATS
                }

                is ISocialMsg -> {
                    SOCIAL
                }

                else -> {
                    null
                }
            }
        }

        fun getByString(string: String?): MsgTypeEnum? {
            if (StrUtil.isEmpty(string)) {
                return null
            }

            for (value in values()) {
                if (value.name == string) {
                    return value
                }
            }

            return null
        }
    }
}
