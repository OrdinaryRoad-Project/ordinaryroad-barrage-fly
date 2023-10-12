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
  <v-hover>
    <template #default="{hover}">
      <v-chip
        outlined
        color="primary"
        :close="hover"
        @click:close="$emit('clickClose')"
      >
        <or-barrage-fly-msg-user-avatar :msg="msg" class="me-1" avatar-class="v-icon--left" />
        <or-barrage-fly-msg-username :msg="msg" />
        ：{{ msg.msg.content }}
      </v-chip>
    </template>
  </v-hover>
</template>
<script>
export default {
  name: 'OrBarrageFlyMsgSuperChat',
  props: {
    msg: {
      type: Object,
      required: true
    }
  }
}
</script>
<style scoped>

</style>
