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
  <or-barrage-fly-task-data-iterator
    ref="dataIterator"
    @getItems="onGetItems"
  >
    <template #default="{ props }">
      <v-container>
        <v-row
          class="d-flex flex-wrap"
          dense
        >
          <v-col
            v-for="item in props.items"
            :key="item.id+item.status"
            cols="12"
            sm="12"
            md="12"
            lg="6"
            xl="4"
          >
            <or-barrage-fly-task-item
              :item="item"
              @taskDeleted="$refs.dataIterator.$refs.dataIterator.getItems()"
              @taskUpdated="$refs.dataIterator.$refs.dataIterator.getItems()"
            />
          </v-col>
        </v-row>
      </v-container>
    </template>
  </or-barrage-fly-task-data-iterator>
</template>

<script>
export default {
  name: 'OrBarrageFlyTaskDataIteratorDefault',
  props: {
    taskCreated: {
      type: Object,
      default: null
    }
  },
  watch: {
    taskCreated () {
      this.$refs.dataIterator.$refs.dataIterator.getItems()
    }
  },
  methods: {
    onGetItems ({
      options,
      offset,
      limit,
      sortBy,
      sortDesc
    }) {
      this.$apis.task.page(offset, limit, sortBy, sortDesc, this.$refs.dataIterator.searchParams)
        .then((data) => {
          const records = data.list
          this.$refs.dataIterator.$refs.dataIterator.loadSuccessfully(records, data.total)
          this.$refs.loadMoreFooter && this.$refs.loadMoreFooter.finishLoad()
        })
        .catch(() => {
          this.$refs.dataIterator.$refs.dataIterator.loadFinish()
          this.$refs.loadMoreFooter && this.$refs.loadMoreFooter.finishLoad()
        })
    }
  }
}
</script>

<style scoped>

</style>
