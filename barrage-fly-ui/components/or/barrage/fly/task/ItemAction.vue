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
  <span class="d-flex align-center justify-center">
    <span v-if="!['RUNNING','STOPPED'].includes(taskStatus)">
      {{ taskStatus }}
      <v-progress-circular class="mx-1" width="2" size="24" indeterminate />
    </span>
    <span v-if="['RUNNING'].includes(taskStatus)">
      <v-slide-x-reverse-transition>
        <span v-if="hover">
          <v-btn
            icon
            :href="`/barrage?taskIds=${item.id}`"
            target="_blank"
            @click="$emit('clickExpand')"
          >
            <v-icon>mdi-arrow-expand-all</v-icon>
          </v-btn>
          <v-btn
            text
            @click="$emit('clickDetail')"
          >
            {{ $t('barrageFlyTask.actions.detail') }}
          </v-btn>
        </span>
      </v-slide-x-reverse-transition>
    </span>
    <v-slide-x-reverse-transition>
      <v-btn
        v-if="hover&&['STOPPED'].includes(taskStatus)"
        fab
        text
        small
        color="error"
        @click="this.delete"
      >
        <v-icon>
          mdi-delete-forever
        </v-icon>
      </v-btn>
    </v-slide-x-reverse-transition>
    <v-slide-x-reverse-transition>
      <v-btn
        v-if="hover&&taskStatus==='STOPPED'"
        fab
        small
        text
        @click="$refs.updateTaskDialog.show()"
      >
        <v-icon>
          mdi-pencil
        </v-icon>
      </v-btn>
    </v-slide-x-reverse-transition>
    <v-btn
      v-if="taskStatus==='STOPPED'"
      text
      color="success"
      :loading="starting"
      :disabled="starting"
      @click="start"
    >
      <v-icon left>
        mdi-play
      </v-icon>
      {{ $t('barrageFlyTask.actions.start') }}
    </v-btn>
    <span v-if="taskStatus!=='STOPPED'">
      <v-btn text color="error" @click="stop">
        <v-icon left>
          mdi-stop
        </v-icon>
        {{ $t('barrageFlyTask.actions.stop') }}
      </v-btn>
    </span>

    <or-base-dialog
      ref="updateTaskDialog"
      persistent
      loading
      :title="$t('barrageFlyTask.actions.edit')"
      @onConfirm="edit"
    >
      <or-barrage-fly-task-save-form
        ref="taskSaveForm"
        :preset="item"
        @update="onItemUpdate"
        @submit="$refs.updateTaskDialog.confirm()"
      />
    </or-base-dialog>
  </span>
</template>

<script>
export default {
  name: 'OrBarrageFlyTaskItemAction',
  props: {
    item: {
      type: Object,
      required: true
    },
    hover: {
      type: Boolean,
      required: false
    }
  },
  data: () => ({
    starting: false,
    taskStatusInterval: null,
    taskStatus: null,

    model: null,
    editedItem: null
  }),
  watch: {
    item () {
      this.model = this.item
    }
  },
  mounted () {
    this.model = this.item
    this.taskStatus = this.item.status
    if (this.item.status !== 'STOPPED') {
      this.startTaskStatusInterval()
    }
    this.$once('hook:beforeDestroy', () => {
      this.stopTaskStatusInterval()
    })
  },
  created () {
  },
  methods: {
    startTaskStatusInterval () {
      this.taskStatusInterval = setInterval(() => {
        this.getStatus()
      }, 5000)
    },
    stopTaskStatusInterval () {
      clearInterval(this.taskStatusInterval)
    },
    getStatus () {
      this.$apis.task.getStatus(this.item.id)
        .then((data) => {
          this.taskStatus = data
          if (this.taskStatus === 'STOPPED') {
            this.stopTaskStatusInterval()
          }
        })
    },
    start () {
      this.starting = true
      this.$apis.task.start([this.item.id])
        .then((status) => {
          this.taskStatus = status[0]
          this.$emit('startTask', this.item)
          this.getStatus()
          this.startTaskStatusInterval()
          this.starting = false
        })
        .catch(() => {
          this.starting = false
        })
    },
    stop () {
      this.$dialog({
        loading: true,
        persistent: false,
        content: this.$t('barrageFlyTask.actions.stopHint', [
          [this.item].map((item) => {
            return `/${item.platform}/${item.roomId}`
          }).join(', ')
        ])
      }).then((dialog) => {
        if (dialog.isConfirm) {
          this.$apis.task.stop([this.item.id])
            .then(() => {
              this.$emit('stopTask', this.item)
              this.getStatus()
              dialog.cancel()
            })
            .catch(() => {
              dialog.cancel()
            })
        }
      })
    },
    delete () {
      this.$dialog({
        loading: true,
        persistent: false,
        content: this.$t('barrageFlyTask.actions.deleteHint', [
          [this.item].map((item) => {
            return `/${item.platform}/${item.roomId}`
          }).join(', ')
        ])
      }).then((dialog) => {
        if (dialog.isConfirm) {
          this.$apis.task.delete([this.item.id])
            .then(() => {
              this.$emit('taskDeleted', this.item)
              dialog.cancel()
            })
            .catch(() => {
              dialog.cancel()
            })
        }
      })
    },
    edit () {
      this.$refs.taskSaveForm.validate()
        .then(() => {
          this.$apis.task.update(this.editedItem)
            .then((data) => {
              this.$emit('taskUpdated', data)
              this.model = data
              this.$refs.updateTaskDialog.close()
            })
            .catch(() => {
              this.$refs.updateTaskDialog.cancelLoading()
            })
        })
        .catch(() => {
          this.$refs.updateTaskDialog.cancelLoading()
        })
    },
    onItemUpdate (item) {
      this.editedItem = item
    }
  }
}
</script>

<style scoped>

</style>
