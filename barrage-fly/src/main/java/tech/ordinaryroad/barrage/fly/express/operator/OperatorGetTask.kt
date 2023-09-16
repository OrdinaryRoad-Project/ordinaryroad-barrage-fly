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

package tech.ordinaryroad.barrage.fly.express.operator

import com.ql.util.express.ArraySwap
import com.ql.util.express.InstructionSetContext
import com.ql.util.express.OperateData
import com.ql.util.express.exception.QLException
import org.springframework.stereotype.Component
import tech.ordinaryroad.barrage.fly.dto.BarrageFlyTaskDTO
import tech.ordinaryroad.barrage.fly.dto.BarrageFlyTaskDTO.Companion.toDTO
import tech.ordinaryroad.barrage.fly.express.operator.base.BaseBarrageFlyOperator
import tech.ordinaryroad.barrage.fly.service.BarrageFlyTaskService

/**
 * 根据taskId获取Task
 *
 * @author mjz
 * @date 2023/9/16
 */
@Component
class OperatorGetTask(private val taskService: BarrageFlyTaskService) : BaseBarrageFlyOperator() {
    override fun getNames() = arrayOf("getTask")

    override fun executeInner(parent: InstructionSetContext, list: ArraySwap): OperateData? {
        val taskId = list[0].toString() as String?
        if (taskId.isNullOrBlank()) {
            throw QLException("请检查参数 taskId:${taskId}")
        }
        val findById = taskService.findById(taskId)
        return OperateData(findById.toDTO(true), BarrageFlyTaskDTO::class.java)
    }

}