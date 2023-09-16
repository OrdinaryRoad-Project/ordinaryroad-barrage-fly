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
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tech.ordinaryroad.barrage.fly.express.operator.base.BaseBarrageFlyOperator


/**
 *
 *
 * @author mjz
 * @date 2023/9/15
 */
@Component
class BarrageFlyExpressRunner(private val operators: List<BaseBarrageFlyOperator>) : ExpressRunner() {

    val log = LoggerFactory.getLogger(BarrageFlyExpressRunner::class.java)

    init {
        operators.forEach { operator -> addBarrageFlyOperator(operator) }
    }

    /**
     * 前置操作
     * @return 返回的参数作为filter的参数
     */
    fun executePreMapExpress(express: String?, context: BarrageFlyExpressContext): Any {
        if (express.isNullOrBlank()) {
            return context.getMsg()
        }
        return execute(express, context, null, true, log.isDebugEnabled) ?: context.getMsg()
    }

    fun executeFilterExpress(express: String?, context: BarrageFlyExpressContext): Boolean {
        if (express.isNullOrBlank()) {
            return true
        }
        return (execute(express, context, null, true, log.isDebugEnabled) ?: true) as Boolean
    }

    fun executePostMapExpress(express: String?, context: BarrageFlyExpressContext): Any {
        if (express.isNullOrBlank()) {
            return context.getMsg()
        }
        return execute(express, context, null, true, log.isDebugEnabled) ?: context.getMsg()
    }

    companion object {
        fun BarrageFlyExpressRunner.addBarrageFlyOperator(operator: BaseBarrageFlyOperator) {
            operator.getNames().forEach {
                addFunction(it, operator)
            }
        }
    }
}