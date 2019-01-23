/*
Navicat MySQL Data Transfer

Source Server         : 10.1.85.36
Source Server Version : 50717
Source Host           : 10.1.85.36:3306
Source Database       : dch

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-01-16 10:41:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `org_info`
-- ----------------------------
DROP TABLE IF EXISTS `org_info`;
CREATE TABLE `org_info` (
  `id` varchar(64) NOT NULL,
  `org_name` varchar(1000) DEFAULT NULL,
  `org__code` varchar(50) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `relation_name` varchar(500) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  `modify_by` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of org_info
-- ----------------------------
