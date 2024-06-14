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

# DROP DATABASE IF EXISTS or_barrage_fly;
CREATE DATABASE or_barrage_fly;
USE or_barrage_fly;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for barrage_fly_task
-- ----------------------------
# DROP TABLE IF EXISTS `barrage_fly_task`;
CREATE TABLE `barrage_fly_task`
(
    `id`                   bigint(20)                        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `uuid`                 varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '主键UUID',
    `created_time`         datetime                          NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`            varchar(11) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '创建者',
    `update_time`          datetime                          NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`            varchar(11) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '更新者',

    `platform`             varchar(11) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '平台',
    `room_id`              varchar(50)                       NULL DEFAULT NULL COMMENT '房间号',
    `cookie`               text CHARACTER SET utf8mb4        NULL DEFAULT NULL COMMENT 'Cookie',
    `msg_pre_map_express`  text CHARACTER SET utf8mb4        NULL DEFAULT NULL COMMENT '消息前置处理规则引擎脚本',
    `msg_filter_express`   text CHARACTER SET utf8mb4        NULL DEFAULT NULL COMMENT '消息过滤规则引擎脚本',
    `msg_post_map_express` text CHARACTER SET utf8mb4        NULL DEFAULT NULL COMMENT '消息后置处理规则引擎脚本',
    `client_config_json`   text CHARACTER SET utf8mb4        NULL DEFAULT NULL COMMENT 'Json序列化后的Client配置类',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `barrage_fly_task_uuid_uindex` (`uuid`) USING BTREE,
    INDEX `barrage_fly_task_platform_room_id_uindex` (`platform`, `room_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '弹幕转发任务实体类'
  ROW_FORMAT = Dynamic;

# since v1.0.8
ALTER TABLE `barrage_fly_task`
    ADD `remark` VARCHAR(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '备注' AFTER `room_id`;

# since v1.2.0
ALTER TABLE `barrage_fly_task`
    DROP `client_config_json`,
    ADD `socks5_proxy_host`     VARCHAR(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'Socks5代理——地址' AFTER `msg_post_map_express`,
    ADD `socks5_proxy_port`     MEDIUMINT                          NULL DEFAULT NULL COMMENT 'Socks5代理——端口' AFTER `socks5_proxy_host`,
    ADD `socks5_proxy_username` VARCHAR(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'Socks5代理——用户名' AFTER `socks5_proxy_port`,
    ADD `socks5_proxy_password` VARCHAR(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'Socks5代理——密码' AFTER `socks5_proxy_username`;

SET FOREIGN_KEY_CHECKS = 1;