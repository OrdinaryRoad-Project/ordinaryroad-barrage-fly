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
  <!-- 设置侧边栏 -->
  <v-navigation-drawer
    v-model="localRightDrawerModel"
    width="310"
    right
    temporary
    fixed
  >
    <v-toolbar>
      <v-toolbar-title>{{ $t('more') }}</v-toolbar-title>
      <v-spacer />
      <v-btn
        icon
        href="https://github.com/OrdinaryRoad-Project/barrage-fly"
        target="_blank"
      >
        <v-icon>mdi-github</v-icon>
      </v-btn>
      <v-btn
        icon
        @click.stop="toggleRightDrawerModel"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>

    <!-- 主题设置 -->
    <v-container>
      <div>
        <div class="text-subtitle-2 font-weight-black">
          {{ $t('theme') }}
        </div>
        <v-item-group v-model="selectedThemeOptionModel" mandatory>
          <v-container>
            <v-row>
              <v-col
                v-for="(themeOption,index) in themeOptions"
                :key="themeOption.title"
                cols="6"
                class="pa-1"
                @click="click(index)"
              >
                <v-item v-slot="{ active, toggle }">
                  <v-card
                    flat
                    :color="active ? 'primary' : $vuetify.theme.dark? 'grey darken-3':'grey lighten-3'"
                    :dark="active"
                    class="py-3 px-4 d-flex align-center justify-space-between"
                    height="50"
                    @click="toggle"
                  >
                    <span>{{ $t(themeOption.titleKey) }}</span>
                    <v-icon>{{ themeOption.icon }}</v-icon>
                  </v-card>
                </v-item>
              </v-col>
            </v-row>
          </v-container>
        </v-item-group>
      </div>
    </v-container>

    <!-- 国际化设置 -->
    <div v-if="showI18nSetting">
      <v-divider />
      <v-container>
        <div>
          <div class="text-subtitle-2 font-weight-black">
            {{ $t('language') }}
          </div>
          <v-item-group v-model="selectedLocaleOptionModel" mandatory>
            <v-container>
              <v-row>
                <v-col
                  v-for="(localeOption,index) in localeOptions"
                  :key="localeOption"
                  cols="6"
                  class="pa-1"
                  @click.stop="updateLang({
                    value: locales[index],
                    $i18n, $vuetify, $dayjs
                  })"
                >
                  <v-item v-slot="{ active, toggle }">
                    <v-card
                      flat
                      :color="active ? 'primary' : $vuetify.theme.dark? 'grey darken-3':'grey lighten-3'"
                      :dark="active"
                      class="py-3 px-4 d-flex align-center justify-space-between"
                      height="50"
                      @click="toggle"
                    >
                      <span>{{ localeOption }}</span>
                    </v-card>
                  </v-item>
                </v-col>
              </v-row>
            </v-container>
          </v-item-group>
        </div>
      </v-container>
    </div>

    <!-- 反馈 -->
    <v-divider />
    <v-container>
      <div class="text-subtitle-2 font-weight-black">
        {{ $t('other') }}
      </div>
      <v-list>
        <v-list-item
          link
          :href="feedbackUrl"
          target="_blank"
        >
          <v-list-item-content>
            <v-list-item-title>{{ $t('feedback') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <slot name="other-list-item" />
      </v-list>
    </v-container>
  </v-navigation-drawer>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import { urlEncode } from 'ordinaryroad-vuetify/src/utils'

export default {
  name: 'OrSettingsDrawer',
  props: {
    showI18nSetting: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters('app', {
      themeOptions: 'getThemeOptions',
      selectedThemeOption: 'getSelectedThemeOption',
      rightDrawerModel: 'getRightDrawerModel'
    }),
    ...mapGetters('i18n', {
      localeOptions: 'getLocaleOptions',
      locales: 'getLocales',
      local: 'getLocale'
    }),

    localRightDrawerModel: {
      get () {
        return this.rightDrawerModel
      },
      set (val) {
        this.setRightDrawerModel(val)
      }
    },
    selectedThemeOptionModel: {
      get () {
        return this.selectedThemeOption
      },
      set (val) {
        // ignore
      }
    },
    selectedLocaleOptionModel: {
      get () {
        return this.locales.indexOf(this.local)
      },
      set (val) {
        // ignore
      }
    },

    feedbackUrl () {
      if (!process.client) {
        return ''
      }
      const customParams = {
        clientInfo: this.$or.util.getBrowserInfo(),
        clientVersion: this.$config.APP_VERSION
      }
      return `https://txc.qq.com/products/608001?${urlEncode(customParams).slice(1)}`
    }
  },
  methods: {
    ...mapActions('app', {
      setSelectedThemeOption: 'setSelectedThemeOption',
      toggleRightDrawerModel: 'toggleRightDrawerModel',
      setRightDrawerModel: 'setRightDrawerModel'
    }),
    ...mapActions('i18n', {
      updateLang: 'updateLang'
    }),

    click (index) {
      this.setSelectedThemeOption({
        value: index,
        $vuetify: this.$vuetify
      })
    }
  }
}
</script>

<style scoped>

</style>
