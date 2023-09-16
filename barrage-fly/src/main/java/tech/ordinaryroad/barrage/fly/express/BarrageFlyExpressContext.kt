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

package tech.ordinaryroad.barrage.fly.express

import com.ql.util.express.DefaultContext

class BarrageFlyExpressContext : DefaultContext<String, Any>() {

    fun setMsg(msg: Any): BarrageFlyExpressContext {
        put("msg", msg)
        return this
    }

    fun getMsg(): Any {
        return get("msg") as Any
    }

    fun setClientId(clientId: String): BarrageFlyExpressContext {
        put("clientId", clientId)
        return this
    }

    fun getClientId(): String {
        return get("clientId") as String
    }

}