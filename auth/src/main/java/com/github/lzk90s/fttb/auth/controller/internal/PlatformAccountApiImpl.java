package com.github.lzk90s.fttb.auth.controller.internal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.lzk90s.fttb.auth.dao.entity.PlatformAccountEntity;
import com.github.lzk90s.fttb.auth.service.PlatformAccountService;
import com.github.lzk90s.fttb.internal.api.auth.PlatformAccountApi;
import com.github.lzk90s.fttb.internal.api.auth.PlatformAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/platform_account")
public class PlatformAccountApiImpl implements PlatformAccountApi {
    @Autowired
    private PlatformAccountService platformAccountService;

    @Override
    public List<PlatformAccountDTO> listPlatformAccountByUser(String userName) {
        Wrapper<PlatformAccountEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_name", userName);
        var list = platformAccountService.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(s -> PlatformAccountEntity.getConverter().doForward(s))
                .collect(Collectors.toList());
    }
}
