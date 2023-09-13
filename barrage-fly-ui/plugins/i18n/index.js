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

import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)
export default ({
  app,
  store
}) => {
  // Set i18n instance on app
  // This way we can use it in middleware and pages asyncData/fetch
  const enOr = require('ordinaryroad-vuetify/src/locales/en.json')
  const zhHansOr = require('ordinaryroad-vuetify/src/locales/zh-Hans.json')

  const enApp = require('~/locales/en.json')
  const zhHansApp = require('~/locales/zh-Hans.json')

  const en = {
    ...enOr,
    ...enApp
  }
  const zhHans = {
    ...zhHansOr,
    ...zhHansApp
  }
  const keysApp = Object.keys(zhHansApp)
  for (const key in enOr) {
    if (keysApp.includes(key)) {
      console.warn('locales key有冲突', key)
    }
  }

  app.i18n = new VueI18n({
    locale: store.getters['i18n/getLocale'],
    fallbackLocale: 'zh-Hans',
    messages: {
      en,
      'zh-Hans': zhHans
    }
  })
  app.i18n.path = (link) => {
    if (app.i18n.locale === app.i18n.fallbackLocale) {
      return `/${link}`
    }
    return `/${app.i18n.locale}/${link}`
  }
  app.i18n.$t = function (key, array) {
    const messages = app.i18n.messages[app.i18n.locale]
    const keys = key.split('.')
    let message = messages[keys[0]]
    for (let i = 1; i < keys.length; i++) {
      message = message[keys[i]]
    }
    if (array) {
      for (let i = 0; i < array.length; i++) {
        message = message.replace(`{${i}}`, array[i])
      }
    }
    return message
  }
}
