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
import tech.ordinaryroad.live.chat.client.bilibili.msg.base.IBilibiliMsg
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg
import tech.ordinaryroad.live.chat.client.douyin.msg.base.IDouyinMsg
import tech.ordinaryroad.live.chat.client.douyu.msg.base.IDouyuMsg
import tech.ordinaryroad.live.chat.client.huya.msg.base.IHuyaMsg
import tech.ordinaryroad.live.chat.client.kuaishou.msg.base.IKuaishouMsg

/**
 * @author mjz
 * @date 2023/9/9
 */
enum class PlatformEnum(val text: String) {
    BILIBILI("B站"),
    DOUYU("斗鱼"),
    HUYA("虎牙"),
    DOUYIN("抖音"),
    KUAISHOU("快手"),
    ;

    companion object {
        fun getByMsg(msg: IMsg?): PlatformEnum? {
            return when (msg) {
                is IBilibiliMsg -> {
                    BILIBILI
                }

                is IDouyuMsg -> {
                    DOUYU
                }

                is IHuyaMsg -> {
                    HUYA
                }

                is IDouyinMsg -> {
                    DOUYIN
                }

                is IKuaishouMsg -> {
                    KUAISHOU
                }

                else -> {
                    null
                }
            }
        }

        fun getByString(string: String?): PlatformEnum? {
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
