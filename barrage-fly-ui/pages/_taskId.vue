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
  <nuxt-child :task="task" />
</template>

<script>
export default {
  layout ({ route }) {
    return 'empty'
  },
  validate ({ params, query, store }) {
    return params.taskId && params.taskId.trim().length > 0
  },
  asyncData ({ route, $apis, error, app }) {
    const taskId = route.params.taskId || ''
    return $apis.task.findById(taskId)
      .then((data) => {
        return { task: data }
      })
      .catch(() => {
        error({ statusCode: 404, message: app.i18n.$t('barrageFlyTask.actions.taskNotExistHint') })
      })
  },
  data: () => ({
    task: null
  }),
  created () {
  }
}
</script>

<style scoped>

</style>
