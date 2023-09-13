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
  getDashboardMenuItems (state) {
    return state.dashboardMenuItems
  },

  getTitleKey (state) {
    return state.titleKey
  },

  getSelectedThemeOption: (state) => {
    return state.selectedThemeOption || 0
  },

  getThemeOptions (state) {
    return state.themeOptions
  },

  getUserMenuItems (state) {
    return state.userMenuItems
  },

  getDashboardDrawerModel (state) {
    return state.dashboardDrawerModel
  },

  getRightDrawerModel (state) {
    return state.rightDrawerModel
  },

  getSystemPrefersColorScheme (state) {
    return state.systemPrefersColorScheme
  }
}
