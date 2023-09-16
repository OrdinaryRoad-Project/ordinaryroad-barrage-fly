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

import com.ql.util.express.ExpressRunner
import com.ql.util.express.IExpressContext
import org.slf4j.LoggerFactory
import tech.ordinaryroad.barrage.fly.dto.msg.BarrageFlyMsgDTO

/**
 *
 *
 * @author mjz
 * @date 2023/9/15
 */
object BarrageFlyExpressRunner : ExpressRunner() {

    val log = LoggerFactory.getLogger(BarrageFlyExpressRunner::class.java)

    fun executePreMapExpress(express: String?, msgDTO: BarrageFlyMsgDTO, clientId: String = ""): Any {
        return executePreMapExpress(express, BarrageFlyExpressContext().apply {
            setMsg(msgDTO)
            setClientId(clientId)
        }) ?: msgDTO
    }

    /**
     * 前置操作
     * @return 返回的参数作为filter的参数
     */
    fun executePreMapExpress(express: String?, context: BarrageFlyExpressContext): Any? {
        if (express.isNullOrBlank()) {
            return context.get("msg")
        }
        return execute(express, context, null, true, log.isDebugEnabled)
    }

    fun executeFilterExpress(express: String?, msg: Any, clientId: String = ""): Boolean {
        return executeFilterExpress(express, BarrageFlyExpressContext().apply {
            setMsg(msg)
            setClientId(clientId)
        }) ?: true
    }

    fun executeFilterExpress(express: String?, context: IExpressContext<String, Any>): Boolean? {
        if (express.isNullOrBlank()) {
            return true
        }
        return execute(express, context, null, true, log.isDebugEnabled) as Boolean?
    }

    fun executePostMapExpress(express: String?, msg: Any, clientId: String = ""): Any {
        return executePostMapExpress(express, BarrageFlyExpressContext().apply {
            setMsg(msg)
            setClientId(clientId)
        }) ?: msg
    }


    fun executePostMapExpress(express: String?, context: IExpressContext<String, Any>): Any? {
        if (express.isNullOrBlank()) {
            return context.get("msg")
        }
        return execute(express, context, null, true, log.isDebugEnabled)
    }
}