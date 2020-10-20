package com.github.lzk90s.cbec.auth.dao.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.github.lzk90s.cbec.internal.api.auth.PlatformAccountDTO;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@TableName("t_platform_account")
public class PlatformAccountEntity {
    private Long id;
    private String user;
    private String platform;
    private String platformUser;
    private String platformPassword;

    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<PlatformAccountEntity, PlatformAccountDTO> {

        @Override
        public PlatformAccountDTO doForward(PlatformAccountEntity platformAccountEntity) {
            PlatformAccountDTO platformAccountDTO = new PlatformAccountDTO();
            BeanUtils.copyProperties(platformAccountEntity, platformAccountDTO);
            return platformAccountDTO;
        }

        @Override
        public PlatformAccountEntity doBackward(PlatformAccountDTO platformAccountDTO) {
            PlatformAccountEntity platformAccountEntity = new PlatformAccountEntity();
            BeanUtils.copyProperties(platformAccountDTO, platformAccountEntity);
            return platformAccountEntity;
        }
    }
}
