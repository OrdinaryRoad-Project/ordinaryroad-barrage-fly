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
  <v-container ref="main" class="main" :style="{height}">
    <div ref="list">
      <v-list dense>
        <v-list-item v-for="(msg,index) in msgs" :key="index">
          <v-list-item-title v-if="'DANMU'===msg.type">
            <or-avatar size="36" :avatar="msg.msg.userAvatar" :username="msg.msg.username" />
            <span v-if="msg.msg.badgeLevel!==0">
              [{{ msg.msg.badgeLevel }}{{ msg.msg.badgeName }}]
            </span>
            {{ msg.msg.username }}({{ msg.msg.uid }})：{{ msg.msg.content }}
          </v-list-item-title>
          <v-list-item-title v-else-if="msg.type==='GIFT'">
            <or-avatar size="36" :avatar="msg.msg.userAvatar" :username="msg.msg.username" /> {{ msg.msg.username }}({{ msg.msg.uid }}) {{ msg.msg.data?.action ?? '赠送' }} {{
              msg.msg.giftName
            }}({{ msg.msg.giftId }})x{{ msg.msg.giftCount }}({{ msg.msg.giftPrice }})
          </v-list-item-title>
          <v-list-item-title v-else>
            {{ msg.msg }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </div>
  </v-container>
</template>

<script>
import { Flowable } from 'rsocket-flowable'

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
    subscription: null,
    socket: null,
    msgs: []
  }),
  head () {
    return {
      meta: [
        { name: 'referrer', content: 'no-referrer' }
      ]
    }
  },
  mounted () {
    this.$once('hook:beforeDestroy', () => {
      this.subscription && this.subscription.cancel()
      this.socket && this.socket.close()
    })
  },
  created () { // https://githubfast.com/rsocket/rsocket-js/issues/123#issuecomment-988515000
    // 1 建立连接
    this.$store.dispatch('rsocket/connect', { url: this.$config.SUB_BASE_URL })
      .then((socket) => {
        this.socket = socket
        // 订阅连接状态
        this.socket.connectionStatus()
          .subscribe({
            onSubscribe: (sub) => {
              sub.request(0x7FFFFFFF)
            },
            onNext: (status) => {
              // CONNECTED CLOSED
              console.log('onNext', status)
            },
            onComplete: () => {
              console.log('onComplete')
            },
            onError: (e) => {
              console.log('onError', e, e.source)
            }
          })
        // channel是多对0/1/多
        // 使用Flowable不断向Channel请求消息
        this.socket.requestChannel(
          new Flowable((subscriber) => {
            let interval
            // 只发送一条消息
            const max = 1
            let sent = 0
            subscriber.onSubscribe({
              request: (n) => {
                // console.log('request', n)
                // console.log('sent', sent)
                // 只会触发两次，一次1，一次最大值
                if (n === 1) {
                  interval = setInterval(() => {
                    subscriber.onNext(sent)
                    sent++
                    if (sent >= max) {
                      clearInterval(interval)
                    }
                  }, 1000)
                }
              },
              cancel: () => {
                clearInterval(interval)
              }
            })
          })
            .map((n) => {
              // 2 发送订阅Task的请求
              // console.log('map ', n)
              return {
                data: {
                  taskIds: this.taskIds, cmd: 'SUBSCRIBE'
                },
                metadata: {}
              }
            })
        )
          // 3 订阅channel
          .subscribe({
            // channel连接建立
            onSubscribe: (sub) => {
              this.subscription = sub
              // 发送订阅请求，发送的消息个数为最大值（实测只要大于0，request触发一次1，和2147483647）
              this.subscription.request(0x7FFFFFFF)
            },
            onNext: (msg) => {
              if (msg.data.status === 200) {
                console.log('channel ok')
              } else {
                if (this.msgs.length >= 30) {
                  this.msgs.shift()
                }
                this.msgs.push(msg.data)
                this.$nextTick(() => {
                  this.$refs.main.scrollTop = this.$refs.list.scrollHeight
                })
              }
            },
            // channel被server关闭
            onComplete: () => {
              console.log('Received end of server stream.')
            },
            onError: (e) => {
              console.log('An error occurred', e, e.source)
            }
          })
      })
      .catch((reason) => {
        console.log('connect failed', reason)
      })
  },
  methods: {
    onScroll (e) {
      console.log(e.target)
      // this.offsetTop = e.target.scrollTop
    }
  }
}
</script>
<style>
.main {
  width: 100%;
  overflow: auto;
}
</style>
