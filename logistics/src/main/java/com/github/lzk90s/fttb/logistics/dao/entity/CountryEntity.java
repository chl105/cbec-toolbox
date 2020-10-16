package com.github.lzk90s.fttb.logistics.dao.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("t_country")
public class CountryEntity {
    @TableId
    private String id;
    private String bcode;
    private String name;
    private String englishName;
}
