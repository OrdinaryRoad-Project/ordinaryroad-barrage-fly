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

import { SELECTED_THEME_OPTION_KEY, SYSTEM_PREFERS_COLOR_SCHEME_KEY } from 'static/js/utils/cookie/vuex/app'
import { SELECTED_LANG_OPTION_KEY } from 'static/js/utils/cookie/vuex/i18n'

function parseCookieString (string) {
  const cookie = {}
  if (!string) {
    return cookie
  }
  const cookies = string.split('; ')
  // 遍历Cookie，取得需要的值
  cookies.forEach((e) => {
    const split = e.split('=')
    cookie[split[0]] = split[1]
  })
  return cookie
}

function getFromCookie (string, key) {
  return parseCookieString(string)[key]
}

// function getBooleanFromCookie (string, key, defaultValue) {
//   const fromCookie = getFromCookie(string, key)
//   return fromCookie ? fromCookie === 'true' : defaultValue
// }

function getNumberFromCookie (string, key, defaultValue) {
  const fromCookie = getFromCookie(string, key)
  return fromCookie ? Number(fromCookie) : defaultValue
}

function getStringFromCookie (string, key, defaultValue) {
  const fromCookie = getFromCookie(string, key)
  return fromCookie ? String(fromCookie) : defaultValue
}

export function getObjectFromCookie (string, key, defaultValue) {
  const fromCookie = getFromCookie(string, key)
  return fromCookie ? JSON.parse(decodeURIComponent(fromCookie)) : defaultValue
}

export const actions = {
  nuxtServerInit ({ commit }, {
    $dayjs,
    $vuetify,
    $apisServer,
    $access,
    req,
    app
  }) {
    const { store, i18n } = app
    // 初始化，可以获取初始值
    if (typeof req !== 'undefined' && req.headers && req.headers.cookie) {
      const cookieString = req.headers.cookie

      commit('app/UPDATE_SYSTEM_PREFERS_COLOR_SCHEME', getStringFromCookie(cookieString, SYSTEM_PREFERS_COLOR_SCHEME_KEY, store.getters['app/getSystemPrefersColorScheme']))
      commit('app/SET_SELECTED_THEME_OPTION', {
        value: getNumberFromCookie(cookieString, SELECTED_THEME_OPTION_KEY, store.getters['app/getSelectedThemeOption']),
        $vuetify
      })
      commit('app/UPDATE_THEME', { value: store.getters['app/getSelectedThemeOption'], $vuetify })
      commit('i18n/SET_SELECTED_LANG_OPTION', {
        value: getStringFromCookie(cookieString, SELECTED_LANG_OPTION_KEY, store.getters['i18n/getLocale'])
      })
      commit('i18n/UPDATE_LANG', {
        value: store.getters['i18n/getLocale'],
        $i18n: i18n,
        $vuetify,
        $dayjs
      })

      const token = getStringFromCookie(cookieString, 'token', null)
      if (token) {
        return $apisServer.userInfo(token)
          .then((data) => {
            commit('user/SET_USER_INFO', data)
          })
          .catch(() => {
            // Token无效或其他异常，不做任何操作
          })
      }
    }
  }
}
