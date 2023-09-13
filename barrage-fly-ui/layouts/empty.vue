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
  <v-app>
    <v-main>
      <v-fade-transition>
        <nuxt />
      </v-fade-transition>
    </v-main>
  </v-app>
</template>

<script>

export default {
  mounted () {
    // dom初始化完成再初始化主题
    this.$nextTick(() => {
      this.$store.commit('app/UPDATE_THEME', {
        value: this.$store.getters['app/getSelectedThemeOption'],
        $vuetify: this.$vuetify
      })
      this.$store.commit('i18n/UPDATE_LANG', {
        value: this.$store.getters['i18n/getLocale'],
        $i18n: this.$i18n,
        $vuetify: this.$vuetify,
        $dayjs: this.$dayjs
      })
    })
  }
}
</script>
