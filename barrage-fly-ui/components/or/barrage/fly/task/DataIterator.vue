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
  <v-container fluid>
    <or-base-data-iterator
      ref="dataIterator"
      :hide-default-footer="hideDefaultFooter"
      :item-keys="itemKeys"
      :single-select="singleSelect"
      :sort-by="sortBy"
      :sort-desc="sortDesc"
      selectable-key="selectable"
      @getItems="onGetItems"
      @insertItem="onInsertItem"
    >
      <template #searchFormBody>
        <!--        <v-col-->
        <!--          cols="6"-->
        <!--          lg="3"-->
        <!--          md="4"-->
        <!--        >-->
        <!--          <v-select-->
        <!--            v-model="searchParams.status"-->
        <!--            clearable-->
        <!--            :items="taskStatusOptions.items"-->
        <!--            dense-->
        <!--            :loading="taskStatusOptions.loading"-->
        <!--            outlined-->
        <!--            item-text="label"-->
        <!--            item-value="value"-->
        <!--            hide-details="auto"-->
        <!--            :label="$t('barrageFlyTask.status')"-->
        <!--            @change="$refs.dataIterator.searchItems()"-->
        <!--          />-->
        <!--        </v-col>-->
        <v-col
          cols="6"
          lg="3"
          md="4"
        >
          <v-select
            v-model="searchParams.platform"
            :items="platformOptions.data"
            :label="$t('barrageFlyTask.platform')"
            :loading="platformOptions.loading"
            clearable
            dense
            hide-details="auto"
            outlined
            @change="$refs.dataIterator.searchItems()"
          />
        </v-col>
        <v-col
          cols="6"
          lg="3"
          md="4"
        >
          <v-text-field
            v-model.trim="searchParams.roomId"
            :label="$t('barrageFlyTask.roomId')"
            clearable
            dense
            hide-details="auto"
            maxlength="200"
            outlined
            @keydown.enter="$refs.dataIterator.searchItems()"
          />
        </v-col>
        <v-col
          cols="6"
          lg="3"
          md="4"
        >
          <v-text-field
            v-model.trim="searchParams.remark"
            :label="$t('barrageFlyTask.remark')"
            clearable
            dense
            hide-details="auto"
            maxlength="200"
            outlined
            @keydown.enter="$refs.dataIterator.searchItems()"
          />
        </v-col>
      </template>

      <template #default="{ props }">
        <slot :props="props" name="default" />
      </template>

      <!-- { items, originalItemsLength, pagination, options, groupedItems, updateOptions, sort, sortArray, group } -->
      <template
        v-if="hideDefaultFooter"
        #footer="props"
      >
        <slot :props="props" name="footer" />
      </template>

      <!-- { pageStart, pageStop, itemsLength } -->
      <template
        v-if="hideDefaultFooter"
        #[`footer.page-text`]="props"
      >
        <slot :props="props" name="footer.page-text" />
      </template>
    </or-base-data-iterator>
    <or-base-dialog
      ref="createTaskDialog"
      :title="$t('barrageFlyTask.actions.create')"
      loading
      persistent
      @onConfirm="createTask"
    >
      <or-barrage-fly-task-save-form
        ref="taskSaveForm"
        @submit="$refs.createTaskDialog.confirm()"
        @update="onItemUpdate"
      />
    </or-base-dialog>
  </v-container>
</template>

<script>

export default {
  name: 'OrBarrageFlyTaskDataIterator',
  props: {
    sortBy: {
      type: Array,
      default: () => []
    },
    sortDesc: {
      type: Array,
      default: () => []
    },
    /**
     * 选中返回完整Object数组，默认只返回uuid数组
     */
    selectReturnObject: {
      type: Boolean,
      default: false
    },
    singleSelect: {
      type: Boolean,
      default: false
    },
    showSelect: {
      type: Boolean,
      default: false
    },
    showActionsWhenSelecting: {
      type: Boolean,
      default: false
    },
    showBaseHeadersWhenSelecting: {
      type: Boolean,
      default: false
    },
    presetSelectedItems: {
      type: Array,
      default: () => []
    },
    hideDefaultFooter: {
      type: Boolean,
      default: false
    }
  },
  data: () => ({
    editedItem: null,
    searchParams: {
      platform: null,
      roomId: null,
      remark: null
    },
    selectedIndex: -1,
    selectedItem: null,
    taskStatusOptions: {
      loading: true,
      items: []
    },
    platformOptions: {
      loading: true,
      data: []
    }
  }),
  computed: {
    itemKeys () {
      const itemKeys = [
        {
          text: this.$t('barrageFlyTask.platform'),
          value: 'platform'
        },
        {
          text: this.$t('barrageFlyTask.roomId'),
          value: 'roomId'
        }
      ]

      return itemKeys
    }
  },
  watch: {},
  created () {
    this.$apis.task.statuses()
      .then((data) => {
        this.taskStatusOptions.loading = false
        this.taskStatusOptions.items = data
      })
      .catch(() => {
        this.taskStatusOptions.loading = false
      })
    this.$apis.task.platformOptions()
      .then((data) => {
        this.platformOptions.loading = false
        this.platformOptions.data = data
      })
      .catch(() => {
        this.platformOptions.loading = false
      })
  },
  mounted () {
    // this.$refs.dataIterator.getItems()
  },
  methods: {
    onItemUpdate (item) {
      this.editedItem = item
    },
    createTask () {
      this.$refs.taskSaveForm.validate()
        .then(() => {
          this.$apis.task.create(this.editedItem, this.editedItem.startWhenFinished)
            .then((data) => {
              this.$refs.createTaskDialog.close()
              this.$refs.dataIterator.getItems()
            })
            .catch(() => {
              this.$refs.createTaskDialog.cancelLoading()
            })
        })
        .catch(() => {
          this.$refs.createTaskDialog.cancelLoading()
        })
    },
    onResetSearch () {
    },
    onInsertItem () {
      this.selectedIndex = -1
      this.selectedItem = null
      this.$refs.createTaskDialog.show()
    },
    onEditItem ({
      item,
      index
    }) {
      this.selectedIndex = index
      this.selectedItem = Object.assign({}, item)
    },
    onGetItems ({
      options,
      offset,
      limit,
      sortBy,
      sortDesc
    }) {
      this.$emit('getItems', { options, offset, limit, sortBy, sortDesc })
    },
    onItemsSelected (items) {
      this.$emit('itemsSelected', items)
    },
    unSelectItem (item) {
      this.$refs.dataIterator.select({
        item,
        value: false,
        emit: true
      })
    }
  }
}
</script>
