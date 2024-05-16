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
const {WebSocketClient, CMD} = require('../../barrage-fly-ui/plugins/rsocket/WebSocketClient')
const WebSocket = require('ws')

// TODO 修改连接地址
const url = "ws://localhost:9898"
// TODO 修改任务ID
// const taskIds = ["1701164893396299776", "1711738902803615744"]
const taskIds = ["1718481386431119360"]

const payloadData = {
    taskIds,
    cmd: CMD.SUBSCRIBE
}

const client = new WebSocketClient({
    url,
    wsCreator: (url) => {
        return new WebSocket(url);
    }
})
    .onMsg((msgs) => {
        msgs.forEach(msg => {
            msg = msg.data
            const msgDto = msg.msg
            const roomId = msg.roomId
            switch (msg.type) {
                case "DANMU":
                    console.log(`${roomId} 收到弹幕 ${msgDto.badgeLevel !== 0 ? msgDto.badgeLevel + msgDto.badgeName : ""} ${msgDto.username}(${msgDto.uid})：${msgDto.content}`, '头像', msgDto.userAvatar)
                    break
                case "GIFT":
                    console.log(`${roomId} 收到礼物 ${msgDto.badgeLevel !== 0 ? msgDto.badgeLevel + msgDto.badgeName : ""} ${msgDto.username}(${msgDto.uid}) ${msgDto.data?.action ?? "赠送"} ${msgDto.giftName}(${msgDto.giftId})x${msgDto.giftCount}(${msgDto.giftPrice})`, '头像', msgDto.userAvatar, '礼物图片', msgDto.giftImg);
                    break
                default:
                    console.log(`${roomId} 收到消息 ${msg}`)
            }
        })
    })
    .onSystemMsg((msgs) => {
        msgs.forEach(msg => {
            console.log('收到系统消息', msg)
        })
    })
    .onConnected(() => {
        console.log('连接建立成功')
    })
    .onClosed(() => {
        console.log('连接已关闭')
    })

client.connect()
    .then((client) => {
        client.requestChannel(payloadData)
    })

setTimeout(() => {
}, 99999999)
