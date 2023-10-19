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
  <v-avatar
    style="border: 1px solid;"
    :size="size"
    color="grey"
    :class="avatarClass"
  >
    <img :src="userAvatar(msg)" :alt="msg.msg.username">
  </v-avatar>
</template>

<script>
export default {
  name: 'OrBarrageFlyMsgUserAvatar',
  props: {
    avatarClass: {
      type: String,
      default: null
    },
    size: {
      type: [Number, String],
      default: 48
    },
    msg: {
      type: Object,
      required: true
    }
  },
  computed: {
    userAvatar () {
      return (msg) => {
        const msgUserAvatar = msg.msg.userAvatar
        if (!msgUserAvatar || msgUserAvatar === '') {
          switch (msg.platform) {
            case 'HUYA':
              // https://huyaimg.msstatic.com/avatar/1023/6d/28ce3bb6c316cd22c06a83053dd367_180_135.jpg
              // https://huyaimg.msstatic.com/avatar/1012/11/31cf42985b9ed730e7033d9f74d81b_180_135.jpg
              return 'https://huyaimg.msstatic.com/avatar/1023/6d/28ce3bb6c316cd22c06a83053dd367_180_135.jpg'
            default:
              return null
          }
        }

        return msgUserAvatar
      }
    }
  }
}
</script>

<style scoped>

</style>
