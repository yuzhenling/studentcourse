package com.upper6.studentcourse.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求：type=ADMIN 用 sys_user；type=STUDENT 用学号+students 表；type=TEACHER 用工号+teachers 表
 */
@Data
public class LoginRequest {

    /** ADMIN=管理员, STUDENT=学生, TEACHER=教师；不传或 ADMIN 时按 sys_user 校验 */
    private String type = "ADMIN";

    @NotBlank(message = "用户名/学号/工号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
