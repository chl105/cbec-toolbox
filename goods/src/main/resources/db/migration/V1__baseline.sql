drop table IF EXISTS `t_goods`;
create TABLE `t_goods`  (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `subject` varchar(512) NOT NULL COMMENT '名字',
  `category` varchar(128) NOT NULL DEFAULT '' COMMENT '类别',
  `platform` varchar(128) NOT NULL COMMENT '商品所在平台',
  `detail_url` varchar(512) NOT NULL COMMENT 'url',
  `image_url` varchar(512) NOT NULL COMMENT '图片url',
  `price` DECIMAL(5,2) NOT NULL COMMENT '价格',
  `seller_name` varchar(128) DEFAULT '' COMMENT '卖家名',
  `seller_shop_url` varchar(512) DEFAULT '' COMMENT '卖家店铺url',
  `purchased` tinyint(1) DEFAULT 0 COMMENT '是否已经采购',
  `add_time` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `update_time` timestamp NOT NULL DEFAULT current_timestamp() ON update current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '商品表';

drop table IF EXISTS `t_ecommerce_platform`;
create TABLE `t_ecommerce_platform` (
  `name` varchar(36) NOT NULL COMMENT '电子商务平台名称',
  `description` varchar(128) NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB COMMENT = '电商平台表';

insert into t_ecommerce_platform(`name`, `description`) VALUES ('VOVA', 'VOVA');


drop table IF EXISTS `t_goods_supplier`;
create TABLE `t_goods_supplier`  (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `goods_id` varchar(64) NOT NULL COMMENT '商品id',
  `subject` varchar(512) NOT NULL COMMENT '名字',
  `category` varchar(128) NOT NULL DEFAULT '' COMMENT '类别',
  `platform` varchar(128) NOT NULL COMMENT '商品所在平台',
  `detail_url` varchar(512) NOT NULL COMMENT 'url',
  `image_url` varchar(512) NOT NULL COMMENT '图片url',
  `price` DECIMAL(5,2) NOT NULL COMMENT '价格',
  `seller_name` varchar(128) DEFAULT '' COMMENT '卖家名',
  `seller_shop_url` varchar(512) DEFAULT '' COMMENT '卖家店铺url',
  `purchased` tinyint(1) DEFAULT 0 COMMENT '是否已经采购',
  `add_time` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `update_time` timestamp NOT NULL DEFAULT current_timestamp() ON update current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '商品表';