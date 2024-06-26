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
  <div>
    <v-alert
      class="my-2"
      dense
      dismissible
    >
      <div class="text-center overflow-auto">
        点赞数：{{ roomStatsMsg?.likedCount }} | 当前观看人数：{{ roomStatsMsg?.watchingCount }} | 累计观看人数：{{ roomStatsMsg?.watchedCount }}
      </div>
    </v-alert>
    <or-barrage-fly-msg-list ref="msgList" :height="height" />
  </div>
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
    roomStatsMsg: {
      likedCount: '-',
      watchingCount: '-',
      watchedCount: '-'
    },
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
    if (process.client) {
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
            if (msg.data.type === 'ROOM_STATS') {
              const { likedCount, watchingCount, watchedCount } = msg.data.msg
              if (likedCount) { this.roomStatsMsg.likedCount = likedCount }
              if (watchingCount) { this.roomStatsMsg.watchingCount = watchingCount }
              if (watchedCount) { this.roomStatsMsg.watchedCount = watchedCount }
            } else {
              this.$refs.msgList.pushMsg(msg)
            }
          })
        })

      this.webSocketClient.connect()
        .then((webSocketClient) => {
          webSocketClient.requestChannel({
            taskIds: this.taskIds
          }, { cmd: CMD.SUBSCRIBE })
        })
    }
  },
  methods: { }
}
</script>

<style>
</style>
