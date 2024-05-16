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
  <div class="d-flex align-center">
    <or-barrage-fly-msg-user-avatar :msg="msg" size="36" class="me-2" />
    <or-barrage-fly-msg-user-badge :msg="msg" />
    <or-barrage-fly-msg-username :msg="msg" />
    <span v-if="msg.msg.socialAction==='SHARE'">分享了直播间</span>
    <span v-else-if="msg.msg.socialAction==='SUBSCRIBE'">关注了主播</span>
    <span v-else>{{ msg.msg.socialAction }}</span>
  </div>
</template>
<script>
export default {
  name: 'OrBarrageFlyMsgSocial',
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
