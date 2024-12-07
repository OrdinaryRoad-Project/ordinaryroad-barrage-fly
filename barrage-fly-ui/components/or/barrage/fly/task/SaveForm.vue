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
  <v-form ref="form">
    <v-row>
      <v-col v-if="preset.id" cols="12" md="4">
        <v-text-field
          v-model="model.id"
          readonly
          label="taskId"
        />
      </v-col>
      <v-col cols="12" md="4">
        <v-select
          v-model="model.platform"
          :label="$t('barrageFlyTask.platform')"
          :items="platformOptions.data"
          :loading="platformOptions.loading"
          :rules="[$or.rules.required]"
        />
      </v-col>
      <v-col cols="12" md="8">
        <v-text-field
          v-model="model.roomId"
          :rules="[$or.rules.notBlank,$or.rules.max50Chars]"
          :label="$t('barrageFlyTask.roomId')"
          @keydown.enter="$emit('submit')"
        />
      </v-col>
      <v-col cols="12" md="12">
        <v-text-field
          v-model="model.remark"
          :rules="[$or.rules.max200Chars]"
          :label="$t('barrageFlyTask.remark')"
        />
      </v-col>
    </v-row>
    <v-textarea
      v-model="model.cookie"
      :label="$t('barrageFlyTask.cookie')"
      hint="浏览器Cookie，一般只有要发送弹幕时才需要（B站未设置Cookie无法查看用户信息）"
    />

    <v-expansion-panels
      class="mt-2 pa-0 v-sheet--outlined"
      flat
      hover
    >
      <v-expansion-panel>
        <v-expansion-panel-header class="pa-0 pe-4">
          <v-toolbar-title class="v-card__title">
            Client设置
          </v-toolbar-title>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <div>
            <div
              v-for="(config,index) in platformConfigs.data.filter(item=>item.platform===model.platform)[0]?.configs"
              :key="index"
            >
              <div v-if="config.options">
                <v-radio-group
                  v-model="model[config.key]"
                  persistent-hint
                  row
                  :label="config.label"
                  :hint="config.hint"
                >
                  <v-radio
                    v-for="(option,optionIndex) in config.options"
                    :key="optionIndex"
                    :label="option.text"
                    :value="option.value"
                  />
                </v-radio-group>
              </div>
              <div v-else>
                input
              </div>
            </div>
          </div>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>

    <v-expansion-panels
      class="mt-2 pa-0 v-sheet--outlined"
      flat
      hover
    >
      <v-expansion-panel>
        <v-expansion-panel-header class="pa-0 pe-4">
          <v-toolbar-title class="v-card__title">
            高级设置
          </v-toolbar-title>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <v-card outlined>
            <v-card-title>弹幕流设置</v-card-title>
            <v-card-text>
              <v-alert
                outlined
                type="info"
                dismissible
              >
                以下设置需要先了解
                <or-link href="https://github.com/alibaba/QLExpress#readme">
                  QLExpress
                </or-link>
                <br>
                <or-link href="https://barragefly.ordinaryroad.tech/guide/msgflow.html#_4-4-一些例子">
                  一些例子
                </or-link>
              </v-alert>
              <v-textarea
                v-model="model.msgPreMapExpress"
                persistent-hint
                hint="参数：msg；返回值：Object，将传递给消息过滤规则中"
                :label="$t('barrageFlyTask.msgPreMapExpress')"
              />
              <v-textarea
                v-model="model.msgFilterExpress"
                persistent-hint
                hint="参数：msg: 前置处理的返回值；返回值：Boolean，false表示不需要这个消息"
                :label="$t('barrageFlyTask.msgFilterExpress')"
              />
              <v-textarea
                v-model="model.msgPostMapExpress"
                persistent-hint
                hint="参数：msg: 前置处理的返回值；返回值：Object，将传递给Client"
                :label="$t('barrageFlyTask.msgPostMapExpress')"
              />
            </v-card-text>
            <v-card-actions>
              <v-spacer />
              <v-btn
                text
                color="primary"
                @click="onClickTest"
              >
                测试一下
              </v-btn>
            </v-card-actions>
          </v-card>

          <v-card outlined>
            <v-card-title>Socks5代理设置</v-card-title>
            <v-card-text>
              <v-row>
                <v-col cols="8">
                  <v-text-field
                    v-model="model.socks5ProxyHost"
                    label="地址"
                  />
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="model.socks5ProxyPort"
                    type="number"
                    label="端口"
                  />
                </v-col>
              </v-row>
              <v-text-field
                v-model="model.socks5ProxyUsername"
                label="用户名"
              />
              <v-text-field
                v-model="model.socks5ProxyPassword"
                label="密码"
              />
            </v-card-text>
          </v-card>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
    <v-col cols="12" md="4">
      <v-checkbox
        v-model="model.startWhenFinished"
        :label="$t('barrageFlyTask.startWhenFinished')"
      />
    </v-col>
    <or-base-dialog
      ref="testDialog"
      title="测试一下"
      @onConfirm="$refs.testDialog.close()"
    >
      <or-barrage-fly-task-example-real-time-barrage :task="model" />
    </or-base-dialog>
  </v-form>
</template>

<script>
export default {
  name: 'OrBarrageFlyTaskSaveForm',
  props: {
    preset: {
      type: Object,
      default: () => ({
        platform: 'BILIBILI',
        roomId: null,
        remark: null,
        cookie: null,
        msgPreMapExpress: null,
        msgFilterExpress: null,
        msgPostMapExpress: null,
        socks5ProxyHost: null,
        socks5ProxyPort: null,
        socks5ProxyUsername: null,
        socks5ProxyPassword: null
      })
    }
  },
  data: () => ({
    platformOptions: {
      loading: true,
      data: []
    },
    platformConfigs: {
      loading: true,
      data: []
    },
    model: {}
  }),
  watch: {
    preset: {
      handler (val) {
        this.model = Object.assign({}, val)
      },
      deep: true,
      immediate: true
    },
    model: {
      handler (val) {
        this.$emit('update', val)
      },
      deep: true,
      immediate: true
    }
  },
  created () {
    this.$apis.task.platformOptions()
      .then((data) => {
        this.platformOptions.loading = false
        this.platformOptions.data = data
      })
      .catch(() => {
        this.platformOptions.loading = false
      })
    this.$apis.task.platformConfigs()
      .then((data) => {
        this.platformConfigs.loading = false
        this.platformConfigs.data = data
      })
      .catch(() => {
        this.platformConfigs.loading = false
      })
  },
  mounted () {
  },
  methods: {
    onClickTest () {
      this.$refs.testDialog.show()
    },
    validate () {
      return new Promise((resolve, reject) => {
        if (!this.$refs.form.validate()) {
          reject(Error('failed'))
        } else {
          this.$apis.task.validate(this.model)
            .then(() => {
              resolve()
            })
            .catch(() => {
              reject(Error('failed'))
            })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
