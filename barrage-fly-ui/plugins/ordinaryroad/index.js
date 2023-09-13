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
/* 表单校验规则 */
import rules from 'ordinaryroad-vuetify/src/rules'
/* 自定义工具类 */
import util from 'ordinaryroad-vuetify/src/utils'
/* 自定义常量 */
import {
  BaseMaterialCard,
  OrAvatar,
  OrBaseDataIterator,
  OrBaseDataTable,
  OrBaseDialog,
  OrBaseMenu,
  OrBaseTreeList,
  OrEmpty,
  OrFileField,
  OrInputDialog,
  OrLoadMoreFooter,
  OrNoMoreData,
  OrNotFound,
  OrSearch
} from 'ordinaryroad-vuetify/src/components'
import echarts from './echarts/index'

Vue.prototype.$echarts = echarts

export default function (context, inject) {
  const { app } = context
  const i18n = app.i18n

  rules.init(i18n.$t)

  Vue.prototype.$or = {
    util,
    rules,
    locales: {
      en: require('ordinaryroad-vuetify/src/locales/en.json'),
      zhHans: require('ordinaryroad-vuetify/src/locales/zh-Hans.json')
    }
  }

  inject('or', Vue.prototype.$or)

  // 按需引入
  Vue.component('BaseMaterialCard', BaseMaterialCard)
  Vue.component('OrBaseDataIterator', OrBaseDataIterator)
  Vue.component('OrBaseDataTable', OrBaseDataTable)
  Vue.component('OrBaseMenu', OrBaseMenu)
  Vue.component('OrBaseTreeList', OrBaseTreeList)
  Vue.component('OrEmpty', OrEmpty)
  Vue.component('OrLoadMoreFooter', OrLoadMoreFooter)
  Vue.component('OrNoMoreData', OrNoMoreData)
  Vue.component('OrNotFound', OrNotFound)
  Vue.component('OrSearch', OrSearch)
  Vue.component('OrBaseDialog', OrBaseDialog)
  Vue.component('OrInputDialog', OrInputDialog)
  Vue.component('OrAvatar', OrAvatar)
  Vue.component('OrFileField', OrFileField)
}
