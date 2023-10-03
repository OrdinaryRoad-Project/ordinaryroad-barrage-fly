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
      :sort-by="sortBy"
      :sort-desc="sortDesc"
      :single-select="singleSelect"
      selectable-key="selectable"
      :hide-default-footer="hideDefaultFooter"
      :item-keys="itemKeys"
      @getItems="onGetItems"
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
            clearable
            :items="[{label:'B站',value:'BILIBILI'},{label:'斗鱼',value:'DOUYU'},{label:'虎牙',value:'HUYA'}]"
            dense
            outlined
            item-text="label"
            item-value="value"
            hide-details="auto"
            :label="$t('barrageFlyTask.platform')"
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
            dense
            outlined
            clearable
            hide-details="auto"
            maxlength="200"
            :label="$t('barrageFlyTask.roomId')"
            @keydown.enter="$refs.dataIterator.searchItems()"
          />
        </v-col>
      </template>

      <template #actionsTop>
        <span />
      </template>

      <template #default="{ props }">
        <slot name="default" :props="props" />
      </template>

      <!-- { items, originalItemsLength, pagination, options, groupedItems, updateOptions, sort, sortArray, group } -->
      <template
        v-if="hideDefaultFooter"
        #footer="props"
      >
        <slot name="footer" :props="props" />
      </template>

      <!-- { pageStart, pageStop, itemsLength } -->
      <template
        v-if="hideDefaultFooter"
        #[`footer.page-text`]="props"
      >
        <slot name="footer.page-text" :props="props" />
      </template>
    </or-base-data-iterator>
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
    searchParams: {
      platform: null,
      roomId: null
    },
    selectedIndex: -1,
    selectedItem: null,
    taskStatusOptions: {
      loading: true,
      items: []
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
  },
  mounted () {
    // this.$refs.dataIterator.getItems()
  },
  methods: {
    onResetSearch () {
    },
    onInsertItem () {
      this.selectedIndex = -1
      this.selectedItem = null
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
