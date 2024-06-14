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
    private static final long serialVersionUID = -2610222833794398462L;

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
     * 备注
     *
     * @since v1.0.8
     */
    private String remark;

    /**
     * Cookie
     */
    private String cookie;

    /**
     * 消息前置处理规则引擎脚本
     */
    private String msgPreMapExpress;

    /**
     * 消息过滤规则引擎脚本
     */
    private String msgFilterExpress;

    /**
     * 消息后置处理规则引擎脚本
     */
    private String msgPostMapExpress;

    /**
     * Socks5代理——地址
     *
     * @since v1.2.0
     */
    private String socks5ProxyHost;

    /**
     * Socks5代理——端口
     *
     * @since v1.2.0
     */
    private int socks5ProxyPort;

    /**
     * Socks5代理——用户名
     *
     * @since v1.2.0
     */
    private String socks5ProxyUsername;

    /**
     * Socks5代理——密码
     *
     * @since v1.2.0
     */
    private String socks5ProxyPassword;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getMsgPreMapExpress() {
        return msgPreMapExpress;
    }

    public void setMsgPreMapExpress(String msgPreMapExpress) {
        this.msgPreMapExpress = msgPreMapExpress;
    }

    public String getMsgFilterExpress() {
        return msgFilterExpress;
    }

    public void setMsgFilterExpress(String msgFilterExpress) {
        this.msgFilterExpress = msgFilterExpress;
    }

    public String getMsgPostMapExpress() {
        return msgPostMapExpress;
    }

    public void setMsgPostMapExpress(String msgPostMapExpress) {
        this.msgPostMapExpress = msgPostMapExpress;
    }

    public String getSocks5ProxyHost() {
        return socks5ProxyHost;
    }

    public void setSocks5ProxyHost(String socks5ProxyHost) {
        this.socks5ProxyHost = socks5ProxyHost;
    }

    public int getSocks5ProxyPort() {
        return socks5ProxyPort;
    }

    public void setSocks5ProxyPort(int socks5ProxyPort) {
        this.socks5ProxyPort = socks5ProxyPort;
    }

    public String getSocks5ProxyUsername() {
        return socks5ProxyUsername;
    }

    public void setSocks5ProxyUsername(String socks5ProxyUsername) {
        this.socks5ProxyUsername = socks5ProxyUsername;
    }

    public String getSocks5ProxyPassword() {
        return socks5ProxyPassword;
    }

    public void setSocks5ProxyPassword(String socks5ProxyPassword) {
        this.socks5ProxyPassword = socks5ProxyPassword;
    }

    public String getClientConfigJson() {
        return clientConfigJson;
    }

    public void setClientConfigJson(String clientConfigJson) {
        this.clientConfigJson = clientConfigJson;
    }
}
