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

import appApis from './app/index'
import taskApis from './task/index'
import statsApis from './stats/index'
import userApis from './user/index'

export default function ({ $axios, app }, inject) {
  // 初始化axios
  appApis.initAxios($axios)
  taskApis.initAxios($axios)
  statsApis.initAxios($axios)
  userApis.initAxios($axios)
  const $apis = {
    app: appApis.apis,
    task: taskApis.apis,
    stats: statsApis.apis,
    user: userApis.apis,
    statusColor (item) {
      const type = item.type
      if (type >= 100 && type <= 199) {
        return 'info'
      }
      if (type >= 200 && type <= 299) {
        return 'success'
      }
      if (type >= 300 && type <= 399) {
        return 'warning'
      }
      if (type >= 400 && type <= 499) {
        return 'error'
      }
      if (type >= 500 && type <= 599) {
        return 'error'
      }
      return ''
    }
  }
  // $apis
  inject('apis', $apis)
  // $apisServer
  inject('apisServer', {
    userInfo: (token) => {
      return $axios({
        url: '/user/info',
        method: 'get',
        headers: { token }
      })
    }
  })
}
