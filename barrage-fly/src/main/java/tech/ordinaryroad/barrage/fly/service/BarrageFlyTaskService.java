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

package tech.ordinaryroad.barrage.fly.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import tech.ordinaryroad.barrage.fly.dal.dao.BarrageFlyTaskDAO;
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO;
import tech.ordinaryroad.commons.core.base.request.query.BaseQueryRequest;
import tech.ordinaryroad.commons.mybatis.service.BaseService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;

/**
 * @author mjz
 * @date 2023/9/9
 */
@Service
public class BarrageFlyTaskService extends BaseService<BarrageFlyTaskDAO, BarrageFlyTaskDO> {

    public List<BarrageFlyTaskDO> findAll(BarrageFlyTaskDO barrageFlyTaskDO, BaseQueryRequest baseQueryRequest) {
        WeekendSqls<BarrageFlyTaskDO> sqls = WeekendSqls.custom();

        if (barrageFlyTaskDO.getPlatform() != null) {
            sqls.andEqualTo(BarrageFlyTaskDO::getPlatform, barrageFlyTaskDO.getPlatform());
        }
        if (StrUtil.isNotBlank(barrageFlyTaskDO.getRoomId())) {
            sqls.andLike(BarrageFlyTaskDO::getRoomId, "%" + barrageFlyTaskDO.getRoomId() + "%");
        }

        Example.Builder exampleBuilder = Example.builder(BarrageFlyTaskDO.class)
                .where(sqls);
        return super.findAll(baseQueryRequest, sqls, exampleBuilder);
    }

    public List<String> findAllIds() {
        return dao.selectAllIds();
    }

}
