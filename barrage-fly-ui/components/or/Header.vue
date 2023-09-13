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
  <v-app-bar app fixed clipped-left elevate-on-scroll>
    <v-toolbar-title style="cursor: pointer;" @click="$router.push('/')">
      {{ $t('appName') }}
    </v-toolbar-title>

    <v-spacer />

    <!-- 用户信息 -->
    <or-user-info-menu @clickCreate="$refs.createTaskDialog.show()" />

    <!-- 国际化 -->
    <or-i18n-menu v-if="!$vuetify.breakpoint.smAndDown" />

    <!-- 设置 -->
    <v-tooltip bottom>
      <template #activator="{ on, attrs }">
        <v-btn
          icon
          v-bind="attrs"
          v-on="on"
          @click.stop="$store.dispatch('app/toggleRightDrawerModel')"
        >
          <v-icon>mdi-dots-horizontal</v-icon>
        </v-btn>
      </template>
      <span>{{ $t('more') }}</span>
    </v-tooltip>

    <or-base-dialog
      ref="createTaskDialog"
      loading
      persistent
      width="auto"
      :title="$t('barrageFlyTask.actions.create')"
      @onConfirm="createTask"
    >
      <or-barrage-fly-task-save-form
        ref="taskSaveForm"
        @update="onItemUpdate"
        @submit="$refs.createTaskDialog.confirm()"
      />
    </or-base-dialog>
  </v-app-bar>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'OrHeader',
  data: () => ({
    editedItem: null
  }),
  computed: {
    ...mapGetters('app', {
      selectedThemeOption: 'getSelectedThemeOption'
    }),
    selectedThemeOptionModel: {
      get () {
        return this.selectedThemeOption
      },
      set (val) {
        // ignore
      }
    }
  },
  methods: {
    onItemUpdate (item) {
      this.editedItem = item
    },
    createTask () {
      if (this.$refs.taskSaveForm.validate()) {
        this.$apis.task.create({
          platform: this.editedItem.platform,
          roomId: this.editedItem.roomId,
          clientConfigJson: JSON.stringify({
            roomId: this.editedItem.roomId,
            cookie: this.editedItem.cookie
          })
        })
          .then(() => {
            this.$refs.createTaskDialog.close()
          })
          .catch(() => {
            // 取消loading
            this.$refs.createTaskDialog.cancelLoading()
          })
      } else {
        // 取消loading
        this.$refs.createTaskDialog.cancelLoading()
      }
    }
  }
}
</script>

<style scoped>

</style>
