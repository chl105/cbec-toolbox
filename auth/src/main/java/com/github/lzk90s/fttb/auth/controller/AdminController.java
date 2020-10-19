package com.github.lzk90s.fttb.auth.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.lzk90s.fttb.auth.dao.entity.UserEntity;
import com.github.lzk90s.fttb.auth.service.UserService;
import com.github.lzk90s.fttb.common.rest.Result;
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

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    Result<List<UserEntity>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer start,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        Page<UserEntity> page = new Page<>(start, limit);
        Page<UserEntity> result = userService.selectPage(page, null);
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
