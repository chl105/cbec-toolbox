SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(48)  NOT NULL,
  `resource_ids` varchar(256) NULL DEFAULT NULL,
  `client_secret` varchar(256) NULL DEFAULT NULL,
  `scope` varchar(256) NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) NULL DEFAULT NULL,
  `authorities` varchar(256) NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) NULL DEFAULT NULL,
  `autoapprove` varchar(256) NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('web', NULL, 'web-secret', 'all', 'authorization_code,password,refresh_token,client_credentials', NULL, NULL, NULL, NULL, NULL, NULL);

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `phone` varchar(16) NOT NULL DEFAULT '' COMMENT '手机号',
  `state` tinyint(1) NOT NULL COMMENT '状态（1：turn启用，0：false停用）',
  `email` varchar(64) NOT NULL COMMENT '邮箱',
  `add_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 COMMENT = '后台用户表';

INSERT INTO `t_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '', 1, 'admin@test.com', '2018-09-01 00:00:00', '2018-09-01 00:00:00');

DROP TABLE IF EXISTS `t_platform_account`;
CREATE TABLE `t_platform_account` (
  `user_name` varchar(64) NOT NULL COMMENT '用户名',
  `platform` varchar(32) NOT NULL COMMENT '平台名称',
  `user` varchar(64) NOT NULL COMMENT '平台用户名',
  `password` varchar(64) NOT NULL COMMENT '平台密码'
) ENGINE = InnoDB COMMENT = '平台账户表';