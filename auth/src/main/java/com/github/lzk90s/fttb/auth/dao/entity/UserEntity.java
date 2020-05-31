package com.github.lzk90s.fttb.auth.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("t_user")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String phone;
    private Boolean state;
    private Date addTime;
}
