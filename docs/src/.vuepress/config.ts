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

import {defineUserConfig} from "vuepress";
import theme from "./theme.js";

export default defineUserConfig({
    base: process.env.BUILD_TYPE == "GIT_PAGE" ? '/ordinaryroad-barrage-fly/' : "/",

    lang: "zh-CN",
    title: "Barrage Fly v" + require("../../package.json").version,
    description: "Barrage Fly —— 让弹幕飞 在线文档",
    // https://www.vuepress.cn/config/#head
    head: [
        ["script", {}, `
            var _hmt = _hmt || [];
            (function() {
              var hm = document.createElement("script");
              hm.src = "https://hm.baidu.com/hm.js?1254ec7a1da94a91a0c76d23b6dd39e5";
              var s = document.getElementsByTagName("script")[0]; 
              s.parentNode.insertBefore(hm, s);
            })();
        `]
    ],

    theme,

    // Enable it with pwa
    // shouldPrefetch: false,
});
