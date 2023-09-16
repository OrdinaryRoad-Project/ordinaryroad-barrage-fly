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

import {sidebar} from "vuepress-theme-hope";

export default sidebar({
    "/": [
        "",
        {
            text: "简介",
            icon: "laptop",
            prefix:'intro',
            children: "structure",
        },
        {
            text: "部署",
            icon: "gear",
            prefix:'deploy',
            children: "structure",
        },
        {
            text: "文档",
            icon: "book",
            prefix:'guide',
            children: "structure",
        },
    ],
});
