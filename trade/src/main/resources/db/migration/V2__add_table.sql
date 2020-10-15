DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods`  (
  `id` varchar(16) NOT NULL COMMENT 'id',
  `subject` varchar(512) NOT NULL COMMENT '名字',
  `platform` varchar(128) NOT NULL COMMENT '商品所在平台',
  `detail_url` varchar(512) NOT NULL COMMENT 'url',
  `image_url` varchar(512) NOT NULL COMMENT '图片url',
  `price` DECIMAL(5,2) NOT NULL COMMENT '价格',
  `seller_name` varchar(128) DEFAULT '' COMMENT '卖家名',
  `seller_shop_url` varchar(512) DEFAULT '' COMMENT '卖家店铺url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '城市表';
