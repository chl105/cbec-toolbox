package com.github.lzk90s.fttb.trade.logistics.vendor.yw56;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.lzk90s.fttb.common.util.JsonUtil;
import com.github.lzk90s.fttb.trade.logistics.LogisticsProvider;
import com.github.lzk90s.fttb.trade.model.Address;
import com.github.lzk90s.fttb.trade.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzhikun
 */
@Slf4j
@EnableConfigurationProperties(Yw56Properties.class)
@ConditionalOnProperty(prefix = "com.github.lzk90s.trade.logistics.yw56", name = "enable", havingValue = "true", matchIfMissing = true)
@Component
public class Yw56LogisticsProviderImpl implements LogisticsProvider {
    private static final String DEFAULT_TYPE_LIST = "1,2,3,4";
    private static final String DEFAULT_GOODS_ATTRIBUTE = "1";

    @Autowired
    private Yw56Properties yw56Properties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getVendor() {
        return yw56Properties.getVendor();
    }

    @Override
    public List<Price> getPrice(Address src, Address dst, String types, String goodsAttr, float weight) {
        if (StringUtils.isEmpty(types)) {
            types = DEFAULT_TYPE_LIST;
        }
        if (StringUtils.isEmpty(goodsAttr)) {
            goodsAttr = DEFAULT_GOODS_ATTRIBUTE;
        }
        Yw56GetPriceRequest request = Yw56GetPriceRequest.builder()
                .areaCode(src.getAreaCode())
                .areaName(src.getAreaName())
                .cityId(src.getCityCode())
                .cityName(src.getCityName())
                .countryId(dst.getCountryCode())
                .destination(dst.getCountryName())
                .level1Catalog(goodsAttr)
                .productTypes(types)
                .weight(String.valueOf((int) weight))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(JsonUtil.obj2json(request), headers);

        ResponseEntity<String> resEntity = restTemplate.postForEntity(yw56Properties.getPriceUrl(), entity, String.class);
        if (resEntity.getStatusCode() != HttpStatus.OK || !resEntity.hasBody()) {
            log.error("yw56 http error, status is {}", resEntity.getStatusCodeValue());
            return Collections.emptyList();
        }

        Yw56Response<Yw56ItemList<Yw56Price>> resData = JsonUtil.json2pojo(resEntity.getBody(), new TypeReference<>() {
        });

        if (resData.getResult().getItemCount() == 0) {
            return Collections.emptyList();
        }

        // 解析出物流价格，并按照价格排序
        return resData.getResult().getItems().stream()
                .map(s -> new Yw56Price.ConverterImpl().doForward(s))
                .sorted(Comparator.comparingDouble(Price::getExpense))
                .collect(Collectors.toList());
    }
}
