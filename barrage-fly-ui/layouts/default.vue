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
    <!-- 控制台侧边栏 -->
    <v-navigation-drawer
      v-if="$route.path.startsWith('/dashboard')"
      v-model="localDashboardDrawerModel"
      clipped
      app
    >
      <OrBaseTreeList
        :nav="true"
        :items="accessibleDashboardMenuItems"
      />
    </v-navigation-drawer>

    <!-- 设置 -->
    <or-settings-drawer :show-i18n-setting="$vuetify.breakpoint.smAndDown" />

    <!-- 标题 用户名 -->
    <or-header />

    <v-main>
      <v-container
        fluid
        :class="$route.name==='index'?'pa-0':null"
      >
        <nuxt />
      </v-container>
    </v-main>

    <!-- Footer -->
    <or-footer app absolute />

    <!-- 回到顶部按钮 -->
    <v-fab-transition>
      <v-btn
        v-if="scrollToTopFab"
        :small="$vuetify.breakpoint.smAndDown"
        fab
        fixed
        bottom
        right
        color="accent"
        @click="$vuetify.goTo(0)"
      >
        <v-icon :small="$vuetify.breakpoint.smAndDown">
          mdi-chevron-up
        </v-icon>
      </v-btn>
    </v-fab-transition>
  </v-app>
</template>

<script>
// Utilities
import { mapActions, mapGetters } from 'vuex'

export default {
  data () {
    return {
      scrollToTopFab: false
    }
  },
  head () {
    return {
      htmlAttrs: {
        lang: this.$i18n.locale === 'zh-Hans' ? 'zh-CN' : this.$i18n.locale
      }
    }
  },
  computed: {
    ...mapGetters('app', {
      dashboardDrawerModel: 'getDashboardDrawerModel',
      rightDrawerModel: 'getRightDrawerModel',
      titleKey: 'getTitleKey'
    }),
    localDashboardDrawerModel: {
      get () {
        return this.dashboardDrawerModel
      },
      set (val) {
        this.setDashboardDrawerModel(val)
      }
    }
  },
  created () {
    this.localDashboardDrawerModel = !this.$vuetify.breakpoint.mdAndDown
  },
  mounted () {
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

      window.addEventListener('scroll', this.handleScroll)
    })
  },
  beforeDestroy () {
    window.removeEventListener('scroll', this.handleScroll, false)
  },
  methods: {
    ...mapActions('app', {
      setDashboardDrawerModel: 'setDashboardDrawerModel'
    }),

    handleScroll (event) {
      this.scrollToTopFab = event.target.scrollingElement.scrollTop !== 0
    }
  }
}
</script>
