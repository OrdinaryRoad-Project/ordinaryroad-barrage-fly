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

package tech.ordinaryroad.barrage.fly.dal.dao;

import org.apache.ibatis.annotations.Select;
import tech.ordinaryroad.barrage.fly.dal.entity.BarrageFlyTaskDO;
import tech.ordinaryroad.commons.mybatis.mapper.IBaseMapper;

import java.util.List;

/**
 * 弹幕转发任务DAO
 *
 * @author mjz
 * @date 2023/9/9
 */
public interface BarrageFlyTaskDAO extends IBaseMapper<BarrageFlyTaskDO> {

    @Select("SELECT UUID FROM `barrage_fly_task`")
    List<String> selectAllIds();

}
