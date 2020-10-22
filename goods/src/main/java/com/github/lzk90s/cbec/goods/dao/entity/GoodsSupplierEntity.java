package com.github.lzk90s.cbec.goods.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.github.lzk90s.cbec.internal.api.spider.GoodsInfoDTO;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@TableName("t_goods_supplier")
public class GoodsSupplierEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    private String goodsId;
    private String subject;
    private String category;
    private String platform;
    private String detailUrl;
    private String imageUrl;
    private Float price;
    private String sellerName;
    private String sellerShopUrl;
    private Boolean purchased;

    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<GoodsSupplierEntity, GoodsInfoDTO> {

        @Override
        public GoodsInfoDTO doForward(GoodsSupplierEntity goodsSupplierEntity) {
            GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
            BeanUtils.copyProperties(goodsSupplierEntity, goodsInfoDTO);
            return goodsInfoDTO;
        }

        @Override
        public GoodsSupplierEntity doBackward(GoodsInfoDTO goodsInfoDTO) {
            GoodsSupplierEntity goodsSupplierEntity = new GoodsSupplierEntity();
            BeanUtils.copyProperties(goodsInfoDTO, goodsSupplierEntity);
            return goodsSupplierEntity;
        }

        public GoodsSupplierEntity doBackward(GoodsInfoDTO goodsInfoDTO, String goodsId) {
            var entity = doBackward(goodsInfoDTO);
            entity.setGoodsId(goodsId);
            return entity;
        }
    }
}
