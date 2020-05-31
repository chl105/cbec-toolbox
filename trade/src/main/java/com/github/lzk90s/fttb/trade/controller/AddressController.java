package com.github.lzk90s.fttb.trade.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.lzk90s.fttb.common.rest.Result;
import com.github.lzk90s.fttb.trade.dao.entity.CityEntity;
import com.github.lzk90s.fttb.trade.dao.entity.CountryEntity;
import com.github.lzk90s.fttb.trade.model.Label;
import com.github.lzk90s.fttb.trade.service.CityService;
import com.github.lzk90s.fttb.trade.service.CountryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private CityService cityService;

    @GetMapping("country_list")
    Result<List<CountryEntity>> getCountryList(@RequestParam(required = false) String fuzzy,
                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "50") Integer pageSize) {
        fuzzy = Optional.ofNullable(fuzzy).filter(StringUtils::isNotBlank).orElse("");
        var result = countryService.selectPage(new Page<>(page, pageSize),
                new EntityWrapper<>(new CountryEntity())
                        .like("name", fuzzy).or()
                        .like("bcode", fuzzy).or()
                        .like("english_name", fuzzy));
        return Result.ok(result.getRecords()).total(result.getTotal());
    }

    @GetMapping("city_list")
    Result<List<CityEntity>> getCityList(@RequestParam(required = false) String fuzzy,
                                         @RequestParam(required = false, defaultValue = "0") Integer page,
                                         @RequestParam(required = false, defaultValue = "50") Integer pageSize) {
        fuzzy = Optional.ofNullable(fuzzy).filter(StringUtils::isNotBlank).orElse("");
        var result = cityService.selectPage(new Page<>(page, pageSize),
                new EntityWrapper<>(new CityEntity())
                        .like("name", fuzzy));
        return Result.ok(result.getRecords()).total(result.getTotal());
    }
}
