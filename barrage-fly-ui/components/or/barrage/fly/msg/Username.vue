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

<script>
export default {
  name: 'OrBarrageFlyMsgUsername',
  props: {
    msg: {
      type: Object,
      required: true
    }
  },
  computed: {
    userSpaceUrl () {
      return (msg) => {
        if (msg.platform === 'BILIBILI') {
          return `https://space.bilibili.com/${msg.msg.uid}`
        } else if (msg.platform === 'DOUYU') {
          return `https://yuba.douyu.com/wbapi/web/jumpusercenter?id=${msg.msg.uid}&name=${msg.msg.username}`
        } else if (msg.platform === 'HUYA') {
          return `https://www.huya.com/video/u/${msg.msg.uid}`
        } else {
          return null
        }
      }
    }
  }

}
</script>

<template>
  <or-link :href="userSpaceUrl(msg)" hide-icon>
    {{ msg.msg.username }}
  </or-link>
</template>

<style scoped>

</style>
