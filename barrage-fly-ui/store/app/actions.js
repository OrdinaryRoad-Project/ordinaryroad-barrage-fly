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

export default {
  setTitleKey ({ commit }, value) {
    commit('SET_TITLE_KEY', value)
  },
  setSelectedThemeOption ({ commit }, { value, $vuetify }) {
    commit('SET_SELECTED_THEME_OPTION', { value, $vuetify })
    commit('UPDATE_THEME', { value, $vuetify })
  },
  updateTheme ({ commit }, { value, $vuetify }) {
    commit('UPDATE_THEME', { value, $vuetify })
  },
  setMenuItems ({ commit }, menuItems) {
    commit('SET_MENU_ITEMS', menuItems)
  },
  setDashboardDrawerModel ({ commit }, value) {
    commit('SET_DASHBOARD_DRAWER_MODEL', value)
  },
  toggleDashboardDrawerModel ({ commit }) {
    commit('TOGGLE_DASHBOARD_DRAWER_MODEL')
  },
  setRightDrawerModel ({ commit }, value) {
    commit('SET_RIGHT_DRAWER_MODEL', value)
  },
  toggleRightDrawerModel ({ commit }) {
    commit('TOGGLE_RIGHT_DRAWER_MODEL')
  },
  updateSystemPrefersColorScheme ({ commit }, value) {
    commit('UPDATE_SYSTEM_PREFERS_COLOR_SCHEME', value)
  },
  setConfigurations ({ commit }, value) {
    commit('SET_CONFIGURATIONS', value)
  }
}
