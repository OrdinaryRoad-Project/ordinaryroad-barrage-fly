/*
 * Copyright 2023 OrdinaryRoad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { setSelectedThemeOption, setSystemPrefersColorScheme } from 'static/js/utils/cookie/vuex/app'
import colors from 'vuetify/es5/util/colors'

export default {
  SET_TITLE_KEY: (state, value) => (state.titleKey = value),
  SET_SELECTED_THEME_OPTION (state, {
    value,
    $vuetify
  }) {
    state.selectedThemeOption = value
    setSelectedThemeOption(value)
  },
  UPDATE_THEME: (state, { value, $vuetify }) => {
    switch (value) {
      case 0:
        switch (state.systemPrefersColorScheme) {
          case 'dark':
            $vuetify.theme.themes.dark = {
              primary: colors.blue.darken2,
              accent: colors.grey.darken3,
              secondary: colors.amber.darken3,
              info: colors.teal.lighten1,
              warning: colors.amber.base,
              error: colors.deepOrange.accent4,
              success: colors.green.accent3
            }
            $vuetify.theme.dark = true
            break
          case 'light':
          case 'no-preference':
            $vuetify.theme.themes.light = {
              primary: '#1976D2',
              secondary: '#424242',
              accent: '#82B1FF',
              error: '#FF5252',
              info: '#2196F3',
              success: '#4CAF50',
              warning: '#FFC107'
            }
            $vuetify.theme.dark = false
            break
          case 'no-support':
            // ignore
            break
        }
        break
      case 1:
        $vuetify.theme.themes.light = {
          primary: '#1976D2',
          secondary: '#424242',
          accent: '#82B1FF',
          error: '#FF5252',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FFC107'
        }
        $vuetify.theme.dark = false
        break
      case 2:
        $vuetify.theme.themes.dark = {
          primary: colors.blue.darken2,
          accent: colors.grey.darken3,
          secondary: colors.amber.darken3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3
        }
        $vuetify.theme.dark = true
        break
      case 3:
        $vuetify.theme.themes.light = {
          primary: colors.pink.base,
          secondary: colors.lime.base,
          accent: colors.cyan.base,
          error: colors.red.base,
          warning: colors.yellow.base,
          info: colors.blue.base,
          success: colors.green.base
        }
        $vuetify.theme.dark = false
        break
      case 4:
        $vuetify.theme.themes.dark = {
          primary: colors.pink.base,
          secondary: colors.lime.base,
          accent: colors.cyan.base,
          error: colors.red.base,
          warning: colors.yellow.base,
          info: colors.blue.base,
          success: colors.green.base
        }
        $vuetify.theme.dark = true
        break
      default:
    }
  },
  SET_DASHBOARD_DRAWER_MODEL: (state, value) => {
    state.dashboardDrawerModel = value
  },
  TOGGLE_DASHBOARD_DRAWER_MODEL: (state) => {
    state.dashboardDrawerModel = !state.dashboardDrawerModel
  },
  SET_RIGHT_DRAWER_MODEL: (state, value) => {
    state.rightDrawerModel = value
  },
  TOGGLE_RIGHT_DRAWER_MODEL: (state) => {
    state.rightDrawerModel = !state.rightDrawerModel
  },
  UPDATE_SYSTEM_PREFERS_COLOR_SCHEME: (state, value) => {
    state.systemPrefersColorScheme = value
    setSystemPrefersColorScheme(value)
  }
}
