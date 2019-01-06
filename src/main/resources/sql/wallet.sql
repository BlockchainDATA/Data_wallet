/*
 Navicat Premium Data Transfer

 Source Server         : 海外正式
 Source Server Type    : MySQL
 Source Server Version : 50562
 Source Host           : 47.91.18.95
 Source Database       : wallet

 Target Server Type    : MySQL
 Target Server Version : 50562
 File Encoding         : utf-8

 Date: 12/27/2018 16:14:51 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `address`
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wallet_address` varchar(128) DEFAULT NULL COMMENT '地址',
  `password` varchar(128) DEFAULT NULL,
  `private_key` varchar(256) DEFAULT NULL COMMENT '私钥',
  `addr_type` tinyint(4) DEFAULT NULL COMMENT '地址类型 0:用户 1:系统 2:主账户',
  `nonce` int(11) NOT NULL DEFAULT '0',
  `balance` varchar(128) NOT NULL DEFAULT '0' COMMENT '余额，以ETH为单位',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（加锁用）0:未锁定 1:锁定',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='私有链地址表';

-- ----------------------------
--  Table structure for `address_public`
-- ----------------------------
DROP TABLE IF EXISTS `address_public`;
CREATE TABLE `address_public` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wallet_address` varchar(128) DEFAULT NULL COMMENT '公有链钱包地址',
  `private_key` varchar(256) DEFAULT NULL COMMENT '私钥',
  `addr_type` tinyint(4) DEFAULT NULL COMMENT '地址类型 1:系统 2:主账户',
  `nonce` int(11) DEFAULT NULL,
  `eth_balance` varchar(128) DEFAULT NULL COMMENT 'eth的余额',
  `dta_balance` varchar(255) DEFAULT NULL COMMENT 'DTA的余额',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态( 加锁用）0:未锁定 1:锁定',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='ETH公有链的地址表';

-- ----------------------------
--  Table structure for `block_info`
-- ----------------------------
DROP TABLE IF EXISTS `block_info`;
CREATE TABLE `block_info` (
  `block_number` bigint(20) NOT NULL COMMENT '块高',
  `block_hash` varchar(128) DEFAULT NULL COMMENT '块hash',
  `block_time` datetime DEFAULT NULL COMMENT '块时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`block_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='私有链块信息表';

-- ----------------------------
--  Table structure for `scan_block_config`
-- ----------------------------
DROP TABLE IF EXISTS `scan_block_config`;
CREATE TABLE `scan_block_config` (
  `config_key` varchar(128) NOT NULL,
  `config_value` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='扫块配置表';

-- ----------------------------
--  Table structure for `transaction_history`
-- ----------------------------
DROP TABLE IF EXISTS `transaction_history`;
CREATE TABLE `transaction_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '交易id',
  `transaction_hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `transaction_from` varchar(128) DEFAULT NULL COMMENT 'from地址',
  `transaction_to` varchar(128) DEFAULT NULL COMMENT 'To地址',
  `value` varchar(128) DEFAULT NULL COMMENT '金额',
  `gas_price` varchar(128) DEFAULT NULL,
  `gas_limit` int(11) DEFAULT NULL,
  `gas_used` varchar(128) DEFAULT NULL,
  `nonce` int(11) DEFAULT NULL,
  `transaction_type` tinyint(1) DEFAULT NULL COMMENT '交易类型（IN/OUT） 1：转入 2：转出 3:主账户转出交易 4:私有链转到以太坊公链',
  `block_hash` varchar(128) DEFAULT NULL COMMENT '块Hahs',
  `block_number` varchar(128) DEFAULT NULL COMMENT '块高',
  `sign` varchar(255) DEFAULT NULL,
  `state` tinyint(1) DEFAULT NULL COMMENT '状态 1：未处理，2：交易被签名，3：交易被广播，4：交易成功，5：交易失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=400 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='交易历史表';

-- ----------------------------
--  Table structure for `transaction_order`
-- ----------------------------
DROP TABLE IF EXISTS `transaction_order`;
CREATE TABLE `transaction_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '交易的id',
  `from_addr` varchar(128) DEFAULT NULL COMMENT 'from 地址',
  `to_addr` varchar(128) DEFAULT NULL COMMENT 'to 地址',
  `value` varchar(128) DEFAULT NULL COMMENT '金额',
  `fee` varchar(128) DEFAULT NULL COMMENT '手续费',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态 0:初始1:处理中 2:成功 3:失败,4:拒绝 5:审核通过',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `type` tinyint(1) DEFAULT NULL COMMENT '提现订单类型1:表示用户a->用户b双方都是私有链操作,2:表示地址a(私有链地址)->地址b(以太坊公链地址)交易一方to地址为以太坊公链 3:表示私有链系统地址->转到私有链用户地址(用户获得的金币奖励) 4:内部构建的私有链->私有链交易',
  `memo` varchar(55) DEFAULT NULL COMMENT '备忘录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='交易订单表';

-- ----------------------------
--  Table structure for `withdraw_public`
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_public`;
CREATE TABLE `withdraw_public` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '交易ID',
  `private_transaction_id` varchar(32) DEFAULT NULL COMMENT '系统构建的私有链的交易ID',
  `public_transaction_hash` varchar(255) DEFAULT NULL COMMENT '交易hash',
  `public_from` varchar(255) DEFAULT NULL COMMENT '公有链的from地址',
  `public_to` varchar(255) DEFAULT NULL COMMENT '公有链的to地址',
  `private_from` varchar(255) DEFAULT NULL COMMENT '私有链的from地址',
  `private_to` varchar(255) DEFAULT NULL COMMENT '私有链的to地址',
  `value` varchar(255) DEFAULT NULL COMMENT '金额',
  `gas_price` varchar(128) DEFAULT NULL COMMENT 'gasPrice',
  `gas_limit` varchar(11) DEFAULT NULL COMMENT 'gasLimit',
  `gas_used` varchar(128) DEFAULT NULL COMMENT '消耗的手续费',
  `nonce` int(11) DEFAULT NULL COMMENT 'nonce值',
  `state` varchar(255) DEFAULT NULL COMMENT '状态 1：未处理，2：交易被签名，3：交易被广播，4：交易成功，5：交易失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `block_number` bigint(20) DEFAULT NULL COMMENT '块高',
  `memo` varchar(55) DEFAULT NULL COMMENT '备忘录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='提现至以太坊公有链记录表';

SET FOREIGN_KEY_CHECKS = 1;
