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

package tech.ordinaryroad.barrage.fly.dal.entity;

import tech.ordinaryroad.barrage.fly.constant.PlatformEnum;
import tech.ordinaryroad.barrage.fly.dal.entity.base.BaseBarrageFlyDO;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serial;

/**
 * 弹幕转发任务实体类
 *
 * @author mjz
 * @date 2023/9/9
 */
@Table(name = "barrage_fly_task")
public class BarrageFlyTaskDO extends BaseBarrageFlyDO {

    @Serial
    private static final long serialVersionUID = 3268817951814298976L;

    /**
     * 平台，bilibili/douyu
     */
    @Column
    private PlatformEnum platform;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * Cookie
     */
    private String cookie;

    /**
     * Json序列化后的Client配置类
     */
    private String clientConfigJson;

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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getClientConfigJson() {
        return clientConfigJson;
    }

    public void setClientConfigJson(String clientConfigJson) {
        this.clientConfigJson = clientConfigJson;
    }
}
