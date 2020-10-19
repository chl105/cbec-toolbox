package com.github.lzk90s.cbec.auth.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.lzk90s.cbec.auth.dao.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
