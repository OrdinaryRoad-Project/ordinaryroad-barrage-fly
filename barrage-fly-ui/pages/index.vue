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
              lg="3"
              md="6"
              sm="12"
              xl="3"
            >
              <v-hover :disabled="!countOption.to">
                <template #default="{ hover }">
                  <v-card
                    :elevation="hover?4:countOption.to?2:0"
                    :outlined="!countOption.to"
                    :to="countOption.to"
                    class="transition-swing"
                  >
                    <v-card-title>
                      <v-icon
                        v-if="countOption.icon"
                        color="primary"
                        left
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
                          class="ma-2"
                          size="24"
                          style="position: absolute; right: 16px"
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
          <v-card-title>
            {{ $t('myStats.app.title') }}
            <v-spacer />
            <v-tooltip bottom>
              <template #activator="{ on, attrs }">
                <v-btn
                  :href="`${$config.SPRING_BOOT_ADMIN_BASE_URL}/wallboard`"
                  icon
                  target="_blank"
                  v-bind="attrs"
                  v-on="on"
                >
                  <v-img
                    class="ma-1"
                    src="icon-spring-boot-admin.svg"
                    width="20"
                  />
                </v-btn>
              </template>
              Spring Boot Admin
            </v-tooltip>
          </v-card-title>
          <v-row class="ma-1">
            <v-col
              cols="12"
              lg="6"
              md="12"
              sm="12"
              xl="5"
            >
              <or-barrage-fly-stats-app-heap-memory-line :heap-memory-statuses="heapMemoryStatuses" :loading="loading" />
            </v-col>
            <v-col
              cols="12"
              lg="6"
              md="12"
              sm="12"
              xl="5"
            >
              <or-barrage-fly-stats-app-thread-line :loading="loading" :thread-statuses="threadStatuses" />
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
              lg="6"
              md="12"
              sm="12"
              xl="5"
            >
              <or-barrage-fly-stats-task-status-pie :loading="loading" :task-statuses="taskStatuses" />
            </v-col>
            <v-col
              cols="12"
              lg="6"
              md="12"
              sm="12"
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
  props: {
    taskCreated: {
      type: Object,
      default: null
    }
  },
  data: () => ({
    threadStatuses: {},
    heapMemoryStatuses: {},
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
  watch: {
    taskCreated () {
      this.tasksCountOptions.data = (this.tasksCountOptions.data ?? 0) + 1
    }
  },
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
          this.heapMemoryStatuses = data.heapMemoryStatuses
          this.threadStatuses = data.threadStatuses
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
