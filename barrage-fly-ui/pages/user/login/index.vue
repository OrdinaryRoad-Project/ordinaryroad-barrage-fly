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
  <v-container fluid class="pa-0 fill-height">
    <v-img
      class="align-center"
      style="height: 100vh"
      src="https://api.dujin.org/bing/1920.php"
    >
      <base-material-card
        style="opacity:0.85"
        width="400"
      >
        <template #heading>
          <div class="text-center">
            <span class="d-flex justify-center">
              <h2 class="font-weight-bold mb-2 ms-9">{{ $t('appName') }}</h2>
              <span class="text-caption mb-auto ms-1">{{ $config.APP_VERSION }}</span>
            </span>
          </div>
        </template>

        <template #default>
          <v-form class="mt-10" :readonly="loading">
            <v-text-field
              v-model="model.username"
              :label="$t('barrageFlyUser.username')"
              prepend-icon="mdi-account"
              type="text"
            />
            <v-text-field
              v-model="model.password"
              :label="$t('barrageFlyUser.password')"
              prepend-icon="mdi-lock"
              :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              :type="showPassword ? 'text' : 'password'"
              @click:append="showPassword=!showPassword"
              @keydown.enter="login"
            />
            <div class="d-flex justify-space-between align-center">
              <v-checkbox
                v-model="model.remember"
                :label="$t('barrageFlyUser.actions.remember')"
                :messages="model.remember?'不要在非自己设备上勾选哦':null"
                @keydown.enter="login"
              />
            </div>

            <div class="text-center mt-5">
              <v-btn
                x-large
                rounded
                :outlined="loading"
                :depressed="loading"
                :loading="loading"
                color="primary"
                @click="login"
              >
                {{ $t('barrageFlyUser.actions.login') }}
              </v-btn>
            </div>
          </v-form>
        </template>
      </base-material-card>
    </v-img>
  </v-container>
</template>

<script>

import Cookies from 'js-cookie'

export default {
  layout: 'empty',
  asyncData ({
    store,
    route,
    redirect
  }) {
    const redirectPath = route.query.redirect || '/'
    const userInfo = store.getters['user/getUserInfo']
    if (userInfo) {
      redirect(redirectPath)
    } else {
      return {
        redirect: redirectPath
      }
    }
  },
  data: () => ({
    loading: false,
    showPassword: false,
    model: {
      username: null,
      password: null,
      remember: false
    },
    redirect: '/'
  }),
  head () {
    return {
      title: this.$t('barrageFlyUser.actions.login'),
      titleTemplate: `%s - ${this.$t('appName')}`
    }
  },
  created () {
    // 能进入到login页面说明store/index.js初始化userInfo的token无效
    Cookies.remove('token')
  },
  methods: {
    login () {
      this.loading = true
      const json = JSON.stringify(this.model)
      const JSEncrypt = require('jsencrypt').JSEncrypt
      const encrypt = new JSEncrypt()
      encrypt.setPublicKey(this.$config.RSA_PUBLIC_KEY)
      const encrypted = encrypt.encrypt(json)
      this.$apis.user.login(encrypted)
        .then((data) => {
          this.$store.commit('user/SET_USER_INFO', data.userInfo)
          this.$router.replace(this.redirect)
            .catch(() => {
              this.$router.replace('/')
            })
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
</script>

<style scoped>

</style>
