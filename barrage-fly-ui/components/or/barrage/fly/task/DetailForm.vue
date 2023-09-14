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
  <v-container>
    <v-expansion-panels
      v-model="panel"
      class="mb-2 pa-0 v-sheet--outlined"
      flat
      hover
    >
      <v-expansion-panel>
        <v-expansion-panel-header class="pa-0 pe-4">
          <v-toolbar-title class="v-card__title">
            {{ $t('howToConnect.title') }}
          </v-toolbar-title>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <or-how-to-connect :item="item" />
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>

    <v-card outlined>
      <v-card-title>
        <or-barrage-fly-task-item-room-info
          class="me-2"
          :item="item"
        />
        {{ $t('barrageFlyTask.detail.realTimeBarrage') }}
        <v-spacer />
        <v-btn icon :href="`/${item.id}/barrage/real-time`" target="_blank" @click="$emit('clickExpand')">
          <v-icon>mdi-arrow-expand-all</v-icon>
        </v-btn>
      </v-card-title>
      <or-barrage-fly-task-real-time-barrage :task-ids="[item.id]" />
    </v-card>
  </v-container>
</template>

<script>

export default {
  name: 'OrBarrageFlyTaskDetailForm',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  data: () => ({
    panel: 0
  }),
  mounted () {
  },
  created () {
  },
  methods: {
  }
}
</script>

<style>
</style>
