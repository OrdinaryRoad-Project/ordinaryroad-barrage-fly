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
import com.ql.util.express.exception.QLBizException
import com.ql.util.express.instruction.op.OperatorBase
import tech.ordinaryroad.barrage.fly.constant.BarrageFlyTaskStatusEnum
import tech.ordinaryroad.barrage.fly.context.BarrageFlyTaskContext
import tech.ordinaryroad.barrage.fly.exception.BaseBarrageFlyException

/**
 * 发送弹幕
 *
 * @author mjz
 * @date 2023/9/16
 */
class OperatorSendDanmu : OperatorBase() {

    override fun executeInner(parent: InstructionSetContext, list: ArraySwap): OperateData? {
        val taskId = list[0].toString() as String?
        val danmu = list[1].toString() as String?
        if (taskId.isNullOrBlank() || danmu.isNullOrBlank()) {
            throw QLBizException("请检查参数 taskId:${taskId} danmu:${danmu}")
        }
        val context = BarrageFlyTaskContext.getContext(taskId)
        if ((parent.get(KEY_DO_SEND_DANMU_BOOLEAN) ?: true) as Boolean) {
            if (context == null || context.status != BarrageFlyTaskStatusEnum.RUNNING) {
                throw BaseBarrageFlyException("任务${taskId}未运行")
            }
            context.sendDanmu(danmu)
        }

        return OperateData(true, Boolean::class.java)
    }

    companion object {
        const val KEY_DO_SEND_DANMU_BOOLEAN = "_doSendDanmuBoolean"
        val names = arrayOf("sendDanmu", "发送弹幕")
    }

}