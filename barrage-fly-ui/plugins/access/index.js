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

export default ({
  app,
  store,
  route
}, inject) => {
  const isLogged = () => {
    const userInfo = store.getters['user/getUserInfo']
    return userInfo != null
  }
  const checkLogin = (redirect = route.fullPath) => {
    const logged = isLogged()
    if (!logged) {
      app.$dialog({
        persistent: false,
        content: app.i18n.$t('barrageFlyUser.actions.pleaseLoginHint'),
        confirmText: app.i18n.$t('barrageFlyUser.actions.login')
      }).then(({ isConfirm }) => {
        if (isConfirm) {
          app.router.push({
            path: '/user/login',
            query: { redirect }
          })
        }
      })
    }
    return logged
  }
  inject('access', {
    isLogged,
    checkLogin
  })
}
