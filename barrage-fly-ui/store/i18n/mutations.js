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

import { setSelectedLangOption } from 'static/js/utils/cookie/vuex/i18n'

export default {
  UPDATE_LANG (state, {
    value,
    $i18n,
    $vuetify,
    $dayjs
  }) {
    if (state.locales.includes(value)) {
      $i18n.locale = value
      $vuetify.lang.current = value
      $dayjs.locale(value === 'zh-Hans' ? 'zh-cn' : value)

      setSelectedLangOption(value)

      state.locale = value
    }
  },
  SET_SELECTED_LANG_OPTION (state, { value }) {
    state.locale = value
    setSelectedLangOption(value)
  },
  SET_LOCALE_OPTIONS (state, localeOptions) {
    state.localeOptions = localeOptions
  }
}
