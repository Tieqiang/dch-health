/*
Navicat MySQL Data Transfer

Source Server         : 10.1.85.36
Source Server Version : 50717
Source Host           : 10.1.85.36:3306
Source Database       : dch

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-01-16 10:41:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `research_project`
-- ----------------------------
DROP TABLE IF EXISTS `research_project`;
CREATE TABLE `research_project` (
  `id` varchar(64) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  `modify_by` varchar(64) DEFAULT NULL,
  `project_name` varchar(1000) DEFAULT NULL,
  `org_id` varchar(64) DEFAULT NULL,
  `project_code` varchar(100) DEFAULT NULL,
  `report_person` varchar(64) DEFAULT NULL,
  `template_id` varchar(64) DEFAULT NULL,
  `result_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of research_project
-- ----------------------------
