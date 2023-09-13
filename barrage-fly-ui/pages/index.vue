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
  <div class="ma-2">
    <v-row>
      <v-col>
        <v-card outlined>
          <v-card-title>{{ $t('myStats.title') }}</v-card-title>
          <v-row class="ma-1">
            <v-col
              v-for="countOption in countOptions"
              :key="countOption.to"
              cols="12"
              sm="12"
              md="6"
              lg="3"
              xl="3"
            >
              <v-hover :disabled="!countOption.to">
                <template #default="{ hover }">
                  <v-card
                    :outlined="!countOption.to"
                    :elevation="hover?4:countOption.to?2:0"
                    class="transition-swing"
                    :to="countOption.to"
                  >
                    <v-card-title>
                      <v-icon
                        v-if="countOption.icon"
                        left
                        color="primary"
                      >
                        {{ countOption.icon }}
                      </v-icon>
                      {{ $t(countOption.title) }}
                      <v-spacer />
                      <v-slide-x-reverse-transition>
                        <span
                          v-if="!hover"
                          class="text-h4 font-weight-bold primary--text"
                          style="position: absolute; right: 16px"
                        >{{ countOption.loading ? '-' : countOption.data }}</span>
                        <v-icon
                          v-else
                          style="position: absolute; right: 16px"
                          size="24"
                          class="ma-2"
                        >
                          mdi-chevron-right
                        </v-icon>
                      </v-slide-x-reverse-transition>
                    </v-card-title>
                  </v-card>
                </template>
              </v-hover>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <v-card outlined>
          <v-card-title>{{ $t('myStats.task.title') }}</v-card-title>
          <v-row class="ma-1">
            <v-col
              cols="12"
              sm="12"
              md="12"
              lg="6"
              xl="5"
            >
              <or-barrage-fly-stats-task-status-pie :loading="loading" :task-statuses="taskStatuses" />
            </v-col>
            <v-col
              cols="12"
              sm="12"
              md="12"
              lg="6"
              xl="5"
            >
              <or-barrage-fly-stats-task-clients-line :loading="loading" :task-clients="taskClients" />
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  data: () => ({
    taskStatuses: {},
    taskClients: [],
    loading: true,
    countsInterval: null,
    countOptions: [],
    tasksCountOptions: {
      title: 'myStats.tasksCount',
      to: '/task',
      icon: 'mdi-view-dashboard',
      loading: true,
      data: null
    },
    clientsCountOptions: {
      title: 'myStats.clientsCount',
      to: null,
      icon: 'mdi-devices',
      loading: true,
      data: null
    }
  }),
  created () {
    this.countOptions.push(this.tasksCountOptions)
    this.countOptions.push(this.clientsCountOptions)
    this.counts()
  },
  mounted () {
    this.startCountsInterval()
    this.$once('hook:beforeDestroy', () => {
      this.stopCountsInterval()
    })
  },
  methods: {
    startCountsInterval () {
      this.countsInterval = setInterval(() => {
        this.counts()
      }, 5000)
    },
    counts () {
      this.$apis.stats.counts()
        .then((data) => {
          this.loading = false
          this.tasksCountOptions.loading = false
          this.tasksCountOptions.data = data.tasksCount
          this.clientsCountOptions.loading = false
          this.clientsCountOptions.data = data.clientsCount
          this.taskStatuses = data.taskStatuses
          this.taskClients = data.taskClients
        })
        .catch(() => {
          this.loading = false
        })
    },
    stopCountsInterval () {
      clearInterval(this.countsInterval)
    }
  }
}
</script>
