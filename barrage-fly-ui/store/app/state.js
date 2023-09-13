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

export default () => ({
  dashboardDrawerModel: true,
  rightDrawerModel: false,
  selectedThemeOption: 0,
  dashboardMenuItems: [],
  userMenuItems: [],
  titleKey: null,
  themeOptions: [
    {
      titleKey: 'themeTitles.system',
      icon: 'mdi-desktop-tower-monitor'
    },
    {
      titleKey: 'themeTitles.light',
      icon: 'mdi-white-balance-sunny'
    },
    {
      titleKey: 'themeTitles.dark',
      icon: 'mdi-weather-night'
    },
    {
      titleKey: 'themeTitles.hunk',
      icon: 'mdi-arm-flex'
    },
    {
      titleKey: 'themeTitles.hunkDark',
      icon: 'mdi-arm-flex-outline'
    }
  ],
  systemPrefersColorScheme: 'light'
})
