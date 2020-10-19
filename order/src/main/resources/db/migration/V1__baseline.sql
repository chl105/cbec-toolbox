DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` varchar(16) NOT NULL COMMENT 'id',
  `platform` varchar(64) NOT NULL COMMENT '名字',
  `type` varchar(64) NOT NULL DEFAULT '' COMMENT '类型',
  `confirm_time` datetime NOT NULL COMMENT '确认时间',
  `sn` varchar(128) NOT NULL COMMENT 'sn',
  `num` int(10) DEFAULT 0 COMMENT '数量',
  `price` float(5,2) DEFAULT '' COMMENT '价格',
  `last_notify_time` datetime DEFAULT 0 COMMENT '最后通知时间',
  `add_time` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `update_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '商品表';
