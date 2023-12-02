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
    <v-card>
      <v-fade-transition group class="d-flex overflow-x-auto">
        <or-barrage-fly-msg-super-chat
          v-for="(msg) in superChatMsgs"
          :key="`${msg._id}`"
          :msg="msg"
          class="ma-1 flex-shrink-0"
          @clickClose="superChatMsgs=superChatMsgs.filter((item)=>item!==msg)"
        />
      </v-fade-transition>
    </v-card>
    <v-container
      id="main"
      ref="main"
      class="barrage-fly-msg-list-main"
      :style="{height}"
    >
      <div ref="list" v-scroll:#main="onScroll">
        <v-list dense>
          <v-slide-x-transition group>
            <v-list-item v-for="(msg) in msgs" :key="`${msg._id}`">
              <v-list-item-title v-if="'DANMU'===msg.type">
                <or-barrage-fly-msg-danmu :msg="msg" />
              </v-list-item-title>
              <v-list-item-title v-else-if="'GIFT'===msg.type" class="d-flex align-center">
                <or-barrage-fly-msg-gift :msg="msg" />
              </v-list-item-title>
              <v-list-item-title v-else>
                {{ msg.message ?? msg }}
              </v-list-item-title>
            </v-list-item>
          </v-slide-x-transition>
        </v-list>
        <v-fab-transition>
          <v-btn
            v-if="showFab"
            fab
            x-small
            color="primary"
            absolute
            style="bottom: 10px; right: 25px"
            @click="$vuetify.goTo($refs.list.scrollHeight, { container:$refs.main })"
          >
            <v-icon>mdi-arrow-down</v-icon>
          </v-btn>
        </v-fab-transition>
      </div>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'OrBarrageFlyMsgList',
  props: {
    height: {
      type: String,
      default: '50vh'
    }
  },
  data: () => ({
    reachBottom: true,
    msgs: [],
    superChatMsgs: [],
    showFab: false,
    /**
     * 每10s检查醒目留言是否需要清除
     */
    removeSuperChatMsgInterval: null
  }),
  mounted () {
    this.removeSuperChatMsgInterval = setInterval(() => {
      this.superChatMsgs = this.superChatMsgs.filter((msg) => {
        return msg._expirationDate.isAfter(this.$dayjs())
      })
    }, 10 * 1000)
    this.$once('hook:beforeDestroy', () => {
      this.removeSuperChatMsgInterval && clearInterval(this.removeSuperChatMsgInterval)
    })
  },
  methods: {
    pushMsg (msg) {
      const msgDto = { ...msg.data, _id: this.$or.util.uuid() }
      if (msgDto.type === 'SUPER_CHAT') {
        this.superChatMsgs.push({ ...msgDto, _expirationDate: this.$dayjs().add(msgDto.msg.duration, 's') })
      } else {
        if (!this.reachBottom) {
          // 有新消息
          this.showFab = true
          return
        }
        if (this.msgs.length >= 100) {
          this.msgs.shift()
        }
        this.msgs.push(msgDto)
        this.$nextTick(() => {
          if (this.reachBottom && this.$refs.main) {
            this.$refs.main.scrollTop = this.$refs.list.scrollHeight
          }
        })
      }
    },
    onScroll (e) {
      const { scrollTop, clientHeight, scrollHeight } = e.target
      this.reachBottom = scrollTop + clientHeight >= scrollHeight
      if (this.reachBottom) {
        this.showFab = false
      }
    }
  }
}
</script>

<style>
.barrage-fly-msg-list-main {
  width: 100%;
  overflow: auto;
}
</style>
