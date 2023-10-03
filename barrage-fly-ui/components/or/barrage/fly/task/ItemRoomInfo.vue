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
  <span class="d-flex justify-center align-center">
    <img
      style="height: 48px; width: 48px"
      :style="{padding: iconPadding(item)}"
      :alt="`${item.platform}`"
      :src="`/logo/${item.platform}.svg`"
    >
    <v-toolbar-title>
      <or-link :href="roomUrl" hover-able>
        {{ item.roomId }}
      </or-link>
    </v-toolbar-title>
  </span>
</template>

<script>
export default {
  name: 'OrBarrageFlyTaskItemRoomInfo',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  data: () => ({}),
  computed: {
    iconPadding () {
      return (item) => {
        let padding = null
        switch (item.platform) {
          case 'BILIBILI':
            padding = '10px'
            break
          case 'HUYA':
            padding = '10px'
            break
          default:
        }
        return padding
      }
    },
    roomUrl () {
      if (this.item.platform === 'BILIBILI') {
        return `https://live.bilibili.com/${this.item.roomId}`
      } else if (this.item.platform === 'DOUYU') {
        return `https://www.douyu.com/${this.item.roomId}`
      } else if (this.item.platform === 'HUYA') {
        return `https://www.huya.com/${this.item.roomId}`
      } else {
        return null
      }
    }
  },
  mounted () {},
  created () {
  },
  methods: {}
}
</script>

<style scoped>

</style>
