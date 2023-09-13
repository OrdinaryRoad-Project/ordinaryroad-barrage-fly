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
import darkmodejs from '@assortment/darkmodejs'

export default ({
  app,
  store,
  $vuetify
}, inject) => {
  darkmodejs({
    onChange: (activeTheme, themes) => {
      store.dispatch('app/updateSystemPrefersColorScheme', activeTheme)
      if (store.getters['app/getSelectedThemeOption'] !== 0) {
        return
      }

      switch (activeTheme) {
        case themes.DARK:
          store.dispatch('app/updateTheme', {
            value: 2,
            $vuetify
          })
          break
        case themes.LIGHT:
          store.dispatch('app/updateTheme', {
            value: 1,
            $vuetify
          })
          break
        case themes.NO_PREF:
          break
        case themes.NO_SUPP:
          break
      }
    }
  })
}
