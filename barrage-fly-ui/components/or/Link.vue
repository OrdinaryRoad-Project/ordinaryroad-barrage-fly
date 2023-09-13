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
  <v-hover :disabled="!hoverAble">
    <template #default="{ hover }">
      <span
        :class="hover?'primary--text':null"
      >
        <span
          v-if="text"
          class="transition-swing"
        >
          <slot />
        </span>
        <a
          v-else
          :href="href"
          class="transition-swing"
          :target="target"
          :class="hoverAble?(hover?'primary--text':'text--primary'):null"
          style="text-decoration: none;"
        >
          <slot /><v-icon v-if="!hideIcon&&target==='_blank'" x-small>mdi-arrow-top-right-bold-box-outline</v-icon>
        </a>
      </span>
    </template>
  </v-hover>
</template>

<script>
export default {
  name: 'OrLink',
  props: {
    /**
     * 是否为纯文本样式
     */
    text: {
      type: Boolean,
      default: false
    },
    href: {
      type: String,
      required: true
    },
    target: {
      type: String,
      default: '_blank'
    },
    hideIcon: {
      type: Boolean,
      default: false
    },
    hoverAble: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>

</style>
