package com.github.lzk90s.cbec.logistics.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.lzk90s.cbec.common.rest.Result;
import com.github.lzk90s.cbec.logistics.dao.entity.CountryEntity;
import com.github.lzk90s.cbec.logistics.dao.entity.CityEntity;
import com.github.lzk90s.cbec.logistics.service.CityService;
import com.github.lzk90s.cbec.logistics.service.CountryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地址controller
 */
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
        EntityWrapper<CountryEntity> entityWrapper = new EntityWrapper<>(new CountryEntity());
        if (!StringUtils.isEmpty(fuzzy)) {
            entityWrapper.like("name", fuzzy).or()
                    .like("bcode", fuzzy).or()
                    .like("english_name", fuzzy);
        }

        Page<CountryEntity> result = countryService.selectPage(new Page<>(page, pageSize), entityWrapper);
        return Result.ok(result.getRecords()).total(result.getTotal());
    }

    @GetMapping("city_list")
    Result<List<CityEntity>> getCityList(@RequestParam(required = false) String fuzzy,
                                         @RequestParam(required = false, defaultValue = "0") Integer page,
                                         @RequestParam(required = false, defaultValue = "50") Integer pageSize) {
        var entityWrapper = new EntityWrapper<>(new CityEntity());
        if (!StringUtils.isEmpty(fuzzy)) {
            entityWrapper.like("name", fuzzy);
        }
        Page<CityEntity> result = cityService.selectPage(new Page<>(page, pageSize), entityWrapper);
        return Result.ok(result.getRecords()).total(result.getTotal());
    }
}
