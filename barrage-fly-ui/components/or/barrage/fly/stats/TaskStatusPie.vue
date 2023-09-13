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
  <v-card :loading="loading" flat outlined>
    <v-card-title>
      {{ $t('myStats.task.statuses') }}
    </v-card-title>
    <div v-if="!loading">
      <div
        v-if="options.dataset.source.length"
        ref="div"
        style="width: 100%; height: 400px"
      />
      <div v-else>
        <or-empty style="width: 100%; height: 400px" />
      </div>
    </div>
    <div v-if="loading">
      <v-skeleton-loader tile height="200" type="image" />
      <v-skeleton-loader tile height="200" type="image" />
    </div>
  </v-card>
</template>

<script>

export default {
  name: 'OrBarrageFlyStatsTaskStatusPie',
  props: {
    taskStatuses: {
      type: Object,
      required: true
    },
    loading: {
      type: Boolean,
      required: true
    }
  },
  data: () => ({
    chart: undefined,
    options: {
      dataset: {
        source: []
      },
      tooltip: {},
      xAxis: {
        type: 'category',
        show: false
      },
      yAxis: {},
      series: [
        {
          type: 'pie',
          encode: { itemName: 0, value: 1 }
        }
      ]
    }
  }),
  computed: {
  },
  watch: {
    taskStatuses () {
      this.updateSeries()
    }
  },
  created () {
  },
  mounted () {
  },
  methods: {
    updateSeries () {
      if (!this.taskStatuses) {
        return
      }

      this.options.dataset.source = []
      const statuses = Object.keys(this.taskStatuses)
      for (let i = 0; i < statuses.length; i++) {
        const status = statuses[i]
        this.options.dataset.source.push(
          [status, this.taskStatuses[status].length]
        )
      }

      if (this.options.dataset.source.length) {
        this.$nextTick(() => {
          if (this.chart == null) {
            this.chart = this.$echarts.init(this.$refs.div)
            window.addEventListener('resize', this.chart.resize, true)
          }
          this.chart.setOption(this.options)
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
