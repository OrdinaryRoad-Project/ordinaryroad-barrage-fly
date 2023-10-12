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
    通过WebSocket协议进行连接，使用
    <or-link href="https://rsocket.io">
      RSocket
    </or-link>
    协议进行通信<or-link href="https://barragefly.ordinaryroad.tech/guide/connect.html">
      文档跳转
    </or-link><br>
    消息格式为：{data, metadata}，data使用JSON格式，metadata使用MESSAGE_RSOCKET_COMPOSITE_METADATA格式<br>
    连接地址： ws://{HOST}:{PORT:9898}<br>
    连接成功后，连接Channel，发送订阅SUBSCRIBE/取消订阅SUBSCRIBE请求，格式如下
    <json-viewer
      class="my-1"
      :expand-depth="5"
      :theme="`jv-${$vuetify.theme.dark ? 'dark' : 'light'}`"
      :value="{
        data: {
          taskIds: [item.id],
          cmd: 'SUBSCRIBE'
        },
        metadata: undefined
      }"
      copyable
    >
      <template #copy="{ copied }">
        <v-btn :disabled="copied" depressed small>
          {{ copied ? $t('howToConnect.actions.copied') : $t('howToConnect.actions.copy') }}
        </v-btn>
      </template>
    </json-viewer>
  </div>
</template>

<script>
import JsonViewer from 'vue-json-viewer/ssr'
import 'vue-json-viewer/style.css'

export default {
  name: 'OrHowToConnect',
  components: {
    JsonViewer
  },
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  data: () => ({
    randomClientId: null
  }),
  created () {
    this.randomClientId = this.$or.util.uuid()
  }
}
</script>

<style>
.jv-container .jv-code {
  padding: 0 !important;
}

.jv-container .jv-tooltip.right {
  right: 0 !important;
}

.jv-container .jv-button {
  padding: 0 !important;
}
</style>
