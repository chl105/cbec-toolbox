package com.github.lzk90s.cbec.auth.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.lzk90s.cbec.auth.dao.entity.UserEntity;
import com.github.lzk90s.cbec.auth.service.UserService;
import com.github.lzk90s.cbec.common.rest.Result;
import com.github.lzk90s.cbec.common.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author liuzhikun
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final String DEFAULT_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";
    private static final String ADMIN = "admin";

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    Result<List<UserEntity>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer start,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        var entityWrapper = new EntityWrapper<UserEntity>();
        // admin用户可以查询列表，其他用户只能查询自己
        if (!ADMIN.equals(UserUtil.getUserName())){
            entityWrapper.ne("name", ADMIN);
        } else {
            entityWrapper.eq("name", UserUtil.getUserName());
        }
        Page<UserEntity> result = userService.selectPage(new Page<>(start, limit), entityWrapper);
        return Result.ok(result.getRecords()).total(result.getTotal());
    }

    @PutMapping("/users/{id}")
    Result<Void> updateUser(@PathVariable("id") Long id,
                            @RequestParam @NotBlank String phone) {
        userService.updateById(new UserEntity().setId(id).setPhone(phone));
        return Result.ok();
    }

    @PostMapping("/users/{id}/reset_password")
    Result<Void> resetPassword(@PathVariable("id") Long id, HttpServletResponse response) {
        UserEntity user = userService.selectById(id);
        if (user == null) {
            response.setStatus(404);
            return Result.error(404, "用户不存在");
        }
        UserEntity userEntity = new UserEntity().setId(id).setPassword(DEFAULT_PASSWORD);
        userService.updateById(userEntity);
        return Result.ok();
    }
}
