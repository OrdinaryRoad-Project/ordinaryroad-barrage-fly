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
    <v-hover>
      <template #default="{hover}">
        <v-card hover>
          <v-card-title>
            <v-tooltip
              :value="hover"
              :disabled="!item.remark"
              right
              nudge-top="40"
            >
              <template #activator="{ on, attrs }">
                <or-barrage-fly-task-item-room-info
                  v-bind="attrs"
                  :item="item"
                  v-on="on"
                />
              </template>
              <template #default>
                {{ item.remark }}
              </template>
            </v-tooltip>
            <v-spacer />
            <or-barrage-fly-task-item-action
              :hover="hover"
              :item="item"
              @clickDetail="detail"
              @taskDeleted="$emit('taskDeleted',item)"
              @taskUpdated="onTaskUpdated"
            />
          </v-card-title>
        </v-card>
      </template>
    </v-hover>
    <or-base-dialog
      ref="detailDialog"
      :title="$t('barrageFlyTask.actions.detail')"
      @onConfirm="$refs.detailDialog.close()"
    >
      <or-barrage-fly-task-detail-form
        :item="item"
        @clickExpand="$refs.detailDialog.close()"
      />
    </or-base-dialog>
  </div>
</template>

<script>
export default {
  name: 'OrBarrageFlyTaskItem',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  data: () => ({
  }),
  mounted () {
  },
  created () {
  },
  methods: {
    onTaskUpdated (task) {
      this.$emit('taskUpdated', task)
    },
    detail () {
      this.$refs.detailDialog.show()
    }
  }
}
</script>

<style scoped>

</style>
