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
    <v-row
      v-if="pageItems==null"
    >
      加载失败
    </v-row>

    <or-empty v-else-if="pageItems&&pageItems.total===0" />

    <vue-masonry-wall
      v-else-if="pageItems&&pageItems.list.length>0"
      :items="pageItems.list"
      :options="options"
      :ssr="{columns: 1}"
      @append="append"
    >
      <template #default="{item}">
        <template v-if="item">
          <or-barrage-fly-task-item :item="item" />
        </template>
      </template>
    </vue-masonry-wall>

    <or-load-more-footer
      v-if="pageItems.total!==0"
      ref="loadMoreFooter"
      :show-down-icon="!autoLoadMore"
      class="mt-4"
      :no-more-data="loadMoreOptions.noMoreData"
      @loadMore="getItems"
    />
  </v-container>
</template>

<script>
import VueMasonryWall from 'vue-masonry-wall'

export default {
  name: 'OrBarrageFlyTaskList',
  components: { VueMasonryWall },
  props: {
    itemWidth: {
      type: [String, Number],
      default: 500
    },
    autoLoadMore: {
      type: Boolean,
      default: false
    }
  },
  data: () => ({
    loadMoreOptions: {
      loading: false,
      noMoreData: false
    },
    pageItems: {
      list: [],
      isLastPage: false
    },
    taskStatus: {

    },
    /**
     * 最近append加载时间
     */
    lastAppendTime: 0
  }),
  computed: {
    options () {
      const padding = {
        default: 10
      }
      /* if (this.$vuetify.breakpoint.xs) {
        width = 500
      } else if (this.$vuetify.breakpoint.smAndDown) {
        width = 500
      } else if (this.$vuetify.breakpoint.mdAndDown) {
        width = 500
      } else if (this.$vuetify.breakpoint.lgAndDown) {
        width = 500
      } else if (this.$vuetify.breakpoint.xl) {
        width = 500
      } */
      return { width: this.itemWidth, padding }
    }
  },
  mounted () {
  },
  created () {
    if (this.autoLoadMore) {
      this.lastAppendTime = new Date().getTime()
      // this.$refs.loadMoreFooter.startLoading(true)
      this.getItems(false)
    }
  },
  methods: {
    getItems (loadMore = true) {
      if (loadMore) {
        if (this.loadMoreOptions.noMoreData) {
          // console.log('没有更多数据啦')
          return
        }
        if (this.loadMoreOptions.loading) {
          // console.log('正在加载，请等待')
          return
        }
        // 加载更多
        this.$refs.loadMoreFooter.startLoading(true)
        this.loadMoreOptions.loading = true
      }
      // console.log('开始加载', this.pageItems.current + 1)
      this.$emit('update:loading', true)
      this.$apis.task.page(this.pageItems.list.length, 20)
        .then((data) => {
          if (loadMore) {
            this.$refs.loadMoreFooter.finishLoad()
            // console.log('加载完成', data.current)
            const newList = this.pageItems.list.concat(data.list)
            this.pageItems = {
              ...data,
              list: newList
            }
            if (this.pageItems.isLastPage) {
              this.loadMoreOptions.noMoreData = true
            // console.log('没有更多数据啦')
            }
            this.loadMoreOptions.loading = false
          } else {
            this.pageItems = data
            this.loadMoreOptions = {
              loading: false,
              noMoreData: data.isLastPage
            }
            this.$emit('loadFinish')
          }
          this.$emit('update:loading', false)
          if (this.autoLoadMore) {
            this.lastAppendTime = new Date().getTime()
          }
        })
        .catch(() => {
          if (loadMore) {
            this.$refs.loadMoreFooter.finishLoad()
            this.loadMoreOptions.loading = false
          }
          this.$emit('update:loading', false)
        })
    },
    append () {
      if (this.autoLoadMore) {
        if (new Date().getTime() - this.lastAppendTime >= 1000) {
          this.getItems()
        }
      }
    }
  }
}
</script>
<style scoped>

</style>
