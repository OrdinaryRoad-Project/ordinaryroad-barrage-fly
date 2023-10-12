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
      {{ $t('myStats.app.thread') }}<v-spacer />MAX：<span class="primary--text">{{ maxThread??'-' }}</span>
    </v-card-title>
    <div v-if="!loading">
      <div
        v-if="options.series.length!==0"
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
  name: 'OrBarrageFlyStatsAppHeapMemoryLine',
  props: {
    threadStatuses: {
      type: Object,
      required: true
    },
    loading: {
      type: Boolean,
      required: true
    }
  },
  data: () => ({
    maxThread: null,
    chart: undefined,
    options: {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          animation: false
        }
      },
      xAxis: {
        type: 'time',
        splitLine: {
          show: false
        }
      },
      yAxis: {
        type: 'value',
        boundaryGap: [0, '100%'],
        splitLine: {
          show: false
        }
      },
      series: []
    },
    seriesMap: {}
  }),
  computed: {
  },
  watch: {
    threadStatuses () {
      this.updateSeries()
    }
  },
  created () {
  },
  mounted () {
  },
  methods: {
    updateSeries () {
      if (!this.threadStatuses) {
        return
      }

      const seriesMap = {}
      this.options.series = []
      const now = new Date()
      const keys = Object.keys(this.threadStatuses)
      for (let i = 0; i < keys.length; i++) {
        const key = keys[i]
        const item = this.threadStatuses[key]

        if (key === 'peak') {
          this.maxThread = item
          continue
        }

        const dataToAdd = {
          name: now.toString(),
          value: [now.getTime(), item]
        }
        const seriesMapKey = key
        let seriesMapElement = this.seriesMap[seriesMapKey]
        if (seriesMapElement) {
          if (seriesMapElement.data.length > 100) {
            seriesMapElement.data.shift()
          }
          seriesMapElement.data.push(dataToAdd)
        } else {
          seriesMapElement = {
            name: seriesMapKey,
            type: 'line',
            smooth: true,
            showSymbol: false,
            data: [dataToAdd],
            areaStyle: {},
            emphasis: {
              focus: 'series'
            }
          }
        }
        seriesMap[seriesMapKey] = seriesMapElement
      }
      this.seriesMap = seriesMap
      Object.keys(this.seriesMap).forEach((key) => {
        this.options.series.push(this.seriesMap[key])
      })

      if (this.options.series.length !== 0) {
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
