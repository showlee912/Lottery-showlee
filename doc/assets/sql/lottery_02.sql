/*
 Navicat Premium Dump SQL

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : lottery_02

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 10/04/2025 20:06:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_strategy_export_000
-- ----------------------------
DROP TABLE IF EXISTS `user_strategy_export_000`;
CREATE TABLE `user_strategy_export_000`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `strategy_id` bigint NULL DEFAULT NULL COMMENT '策略ID',
  `strategy_mode` tinyint NULL DEFAULT NULL COMMENT '策略方式（1:单项概率、2:总体概率）',
  `grant_type` tinyint NULL DEFAULT NULL COMMENT '发放奖品方式（1:即时、2:定时[含活动结束]、3:人工）',
  `grant_date` datetime NULL DEFAULT NULL COMMENT '发奖时间',
  `grant_state` int NULL DEFAULT NULL COMMENT '发奖状态',
  `award_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发奖ID',
  `award_type` tinyint NULL DEFAULT NULL COMMENT '奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品）',
  `award_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品名称',
  `award_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品内容「文字描述、Key、码」',
  `uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '防重ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uuid`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户策略计算结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_strategy_export_000
-- ----------------------------
INSERT INTO `user_strategy_export_000` VALUES (1, 'fustack', 100001, 1910299816035082240, 10001, NULL, NULL, NULL, 0, '4', 1, 'AirPods', 'Code', '1910299816035082240', '2025-04-10 19:52:22', '2025-04-10 19:52:22');
INSERT INTO `user_strategy_export_000` VALUES (2, 'fustack', 100001, 1910300364587130880, 10001, NULL, NULL, NULL, 0, '4', 1, 'AirPods', 'Code', '1910300364587130880', '2025-04-10 19:54:33', '2025-04-10 19:54:33');

-- ----------------------------
-- Table structure for user_strategy_export_001
-- ----------------------------
DROP TABLE IF EXISTS `user_strategy_export_001`;
CREATE TABLE `user_strategy_export_001`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `strategy_id` bigint NULL DEFAULT NULL COMMENT '策略ID',
  `strategy_mode` tinyint NULL DEFAULT NULL COMMENT '策略方式（1:单项概率、2:总体概率）',
  `grant_type` tinyint NULL DEFAULT NULL COMMENT '发放奖品方式（1:即时、2:定时[含活动结束]、3:人工）',
  `grant_date` datetime NULL DEFAULT NULL COMMENT '发奖时间',
  `grant_state` int NULL DEFAULT NULL COMMENT '发奖状态',
  `award_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发奖ID',
  `award_type` tinyint NULL DEFAULT NULL COMMENT '奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品）',
  `award_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品名称',
  `award_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品内容「文字描述、Key、码」',
  `uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '防重ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uuid`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户策略计算结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_strategy_export_001
-- ----------------------------

-- ----------------------------
-- Table structure for user_strategy_export_002
-- ----------------------------
DROP TABLE IF EXISTS `user_strategy_export_002`;
CREATE TABLE `user_strategy_export_002`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `strategy_id` bigint NULL DEFAULT NULL COMMENT '策略ID',
  `strategy_mode` tinyint NULL DEFAULT NULL COMMENT '策略方式（1:单项概率、2:总体概率）',
  `grant_type` tinyint NULL DEFAULT NULL COMMENT '发放奖品方式（1:即时、2:定时[含活动结束]、3:人工）',
  `grant_date` datetime NULL DEFAULT NULL COMMENT '发奖时间',
  `grant_state` int NULL DEFAULT NULL COMMENT '发奖状态',
  `award_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发奖ID',
  `award_type` tinyint NULL DEFAULT NULL COMMENT '奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品）',
  `award_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品名称',
  `award_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品内容「文字描述、Key、码」',
  `uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '防重ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uuid`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户策略计算结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_strategy_export_002
-- ----------------------------

-- ----------------------------
-- Table structure for user_strategy_export_003
-- ----------------------------
DROP TABLE IF EXISTS `user_strategy_export_003`;
CREATE TABLE `user_strategy_export_003`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `strategy_id` bigint NULL DEFAULT NULL COMMENT '策略ID',
  `strategy_mode` tinyint NULL DEFAULT NULL COMMENT '策略方式（1:单项概率、2:总体概率）',
  `grant_type` tinyint NULL DEFAULT NULL COMMENT '发放奖品方式（1:即时、2:定时[含活动结束]、3:人工）',
  `grant_date` datetime NULL DEFAULT NULL COMMENT '发奖时间',
  `grant_state` int NULL DEFAULT NULL COMMENT '发奖状态',
  `award_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发奖ID',
  `award_type` tinyint NULL DEFAULT NULL COMMENT '奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品）',
  `award_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品名称',
  `award_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品内容「文字描述、Key、码」',
  `uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '防重ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uuid`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户策略计算结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_strategy_export_003
-- ----------------------------

-- ----------------------------
-- Table structure for user_take_activity
-- ----------------------------
DROP TABLE IF EXISTS `user_take_activity`;
CREATE TABLE `user_take_activity`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `take_id` bigint NULL DEFAULT NULL COMMENT '活动领取ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `activity_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '活动名称',
  `state` tinyint NULL DEFAULT NULL COMMENT '活动单使用状态 0未使用、1已使用',
  `take_date` datetime NULL DEFAULT NULL COMMENT '活动领取时间',
  `take_count` int NULL DEFAULT NULL COMMENT '领取次数',
  `uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '防重ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `strategy_id` bigint NULL DEFAULT NULL COMMENT '策略ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uuid`(`uuid` ASC) USING BTREE COMMENT '防重ID'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户参与活动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_take_activity
-- ----------------------------
INSERT INTO `user_take_activity` VALUES (7, 'fustack', 1910300225990549504, 100001, '活动名', 1, '2025-04-10 11:54:00', 4, 'fustack_100001_4', '2025-04-10 19:54:00', '2025-04-10 19:54:00', 10001);

-- ----------------------------
-- Table structure for user_take_activity_count
-- ----------------------------
DROP TABLE IF EXISTS `user_take_activity_count`;
CREATE TABLE `user_take_activity_count`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `total_count` int NULL DEFAULT NULL COMMENT '可领取次数',
  `left_count` int NULL DEFAULT NULL COMMENT '已领取次数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uId_activityId`(`u_id` ASC, `activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户活动参与次数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_take_activity_count
-- ----------------------------
INSERT INTO `user_take_activity_count` VALUES (1, 'fustack', 100001, 100, 96, '2025-04-10 19:09:19', '2025-04-10 19:09:19');

SET FOREIGN_KEY_CHECKS = 1;
