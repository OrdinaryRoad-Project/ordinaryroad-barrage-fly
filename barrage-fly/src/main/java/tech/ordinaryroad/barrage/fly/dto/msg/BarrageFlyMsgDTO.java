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

package tech.ordinaryroad.barrage.fly.dto.msg;

import tech.ordinaryroad.barrage.fly.constant.MsgTypeEnum;
import tech.ordinaryroad.barrage.fly.constant.PlatformEnum;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;

/**
 * @author mjz
 * @date 2023/9/15
 */
public class BarrageFlyMsgDTO {
    private PlatformEnum platform;
    private String roomId;
    private MsgTypeEnum type;
    private IMsg msg;

    public BarrageFlyMsgDTO(String roomId, IMsg msg) {
        this.roomId = roomId;
        this.msg = msg;
        this.platform = PlatformEnum.Companion.getByMsg(msg);
        this.type = MsgTypeEnum.Companion.getByMsg(msg);
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public MsgTypeEnum getType() {
        return type;
    }

    public void setType(MsgTypeEnum type) {
        this.type = type;
    }

    public IMsg getMsg() {
        return msg;
    }

    public void setMsg(IMsg msg) {
        this.msg = msg;
    }
}
