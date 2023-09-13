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
  <v-menu offset-y open-on-hover>
    <template #activator="{ on, attrs }">
      <v-btn
        depressed
        v-bind="attrs"
        v-on="on"
      >
        <v-icon>mdi-translate</v-icon>
        <v-icon>mdi-chevron-down</v-icon>
      </v-btn>
    </template>
    <v-list>
      <v-list-item
        v-for="(localeOption, index) in localeOptions"
        :key="index"
        @click.stop="updateLang({
          value: locales[localeOptions.indexOf(localeOption)],
          $i18n, $vuetify, $dayjs
        })"
      >
        <v-list-item-content>
          <v-list-item-title>{{ localeOption }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'OrI18nMenu',
  computed: {
    ...mapGetters('i18n', {
      localeOptions: 'getLocaleOptions',
      locales: 'getLocales'
    })
  },
  methods: {
    ...mapActions('i18n', {
      updateLang: 'updateLang'
    })
  }
}
</script>

<style scoped>

</style>
