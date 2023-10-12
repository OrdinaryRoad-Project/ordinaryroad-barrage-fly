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
  <or-barrage-fly-task-real-time-barrage
    :task-ids="taskIds"
    height="100vh"
  />
</template>
<script>
export default {
  layout ({ route }) {
    return 'empty'
  },
  props: {},
  asyncData ({ route }) {
    return {
      taskIds: route.query.taskIds?.split(',') ?? []
    }
  },
  data: () => ({
    taskIds: []
  }),
  head () {
    return {
      meta: [
        { name: 'referrer', content: 'no-referrer' }
      ]
    }
  },
  created () {
  }
}
</script>

<style>

</style>
