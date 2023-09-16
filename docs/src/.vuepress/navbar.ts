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

import {navbar} from "vuepress-theme-hope";

export default navbar([
    "/",
    {
        text: "部署",
        icon: "gear",
        link: "./deploy"
    },
    {
        text: "指南",
        icon: "lightbulb",
        prefix: "/guide/",
        children: [
            {
                text: "连接转发服务",
                icon: "lightbulb",
                link: 'connect'
            },
            {
                text: "消息流的过滤与转换",
                icon: "lightbulb",
                link: 'msgflow'
            },
        ],
    },
    {
        text: "LiveChatClient",
        icon: "star",
        link: "https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client",
    },
]);
