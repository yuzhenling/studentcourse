package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.config.security.AuthPrincipal;
import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.dto.LoginRequest;
import com.upper6.studentcourse.model.dto.LoginResponse;
import com.upper6.studentcourse.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口：登录、登出、修改密码
 */
@Tag(name = "认证")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "登出（前端清除 Token 即可）")
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.ok(null);
    }

    @Operation(summary = "修改密码（学生/教师/管理员均可用，按角色更新对应表）")
    @PutMapping("/password")
    public ApiResponse<Void> updatePassword(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody Map<String, String> body) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        if (oldPassword == null || newPassword == null) {
            return ApiResponse.fail(400, "缺少 oldPassword 或 newPassword");
        }
        authService.updatePasswordByRole(principal, oldPassword, newPassword);
        return ApiResponse.ok(null);
    }
}
