<!--
  - Copyright 2023 OrdinaryRoad
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <or-barrage-fly-msg-list ref="msgList" :height="height" />
</template>

<script>
import { CMD, WebSocketClient } from '@/plugins/rsocket/WebSocketClient'

export default {
  name: 'OrBarrageFlyTaskRealTimeBarrage',
  props: {
    taskIds: {
      type: Array,
      required: true
    },
    height: {
      type: String,
      default: '50vh'
    }
  },
  data: () => ({
    webSocketClient: null
  }),
  computed: {},
  mounted () {
    this.$once('hook:beforeDestroy', () => {
      this.webSocketClient && this.webSocketClient.destroy()
    })
  },
  created () {
    // https://githubfast.com/rsocket/rsocket-js/issues/123#issuecomment-988515000
    this.webSocketClient = new WebSocketClient({
      url: this.$config.SUB_BASE_URL
    })
      .onConnected(() => {
        this.$refs.msgList.pushMsg({ data: { message: '连接建立成功' } })
      })
      .onClosed(() => {
        this.$refs.msgList.pushMsg({ data: { message: '连接已关闭' } })
      })
      .onSystemMsg((msgs) => {
        msgs.forEach((msg) => {
          this.$refs.msgList.pushMsg(msg)
        })
      })
      .onMsg((msgs) => {
        msgs.forEach((msg) => {
          this.$refs.msgList.pushMsg(msg)
        })
      })

    this.webSocketClient.connect()
      .then((webSocketClient) => {
        webSocketClient.requestChannel({
          taskIds: this.taskIds
        }, { cmd: CMD.SUBSCRIBE })
      })
  },
  methods: { }
}
</script>

<style>
</style>
