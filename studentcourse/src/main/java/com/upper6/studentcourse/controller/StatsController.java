package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.config.security.AuthPrincipal;
import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.dto.StatsDTO;
import com.upper6.studentcourse.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计接口：管理员首页、教师首页
 */
@Tag(name = "统计")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "管理员首页统计")
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<StatsDTO.AdminDashboard> adminDashboard() {
        return ApiResponse.ok(statsService.getAdminDashboard());
    }

    @Operation(summary = "教师首页统计（总体通过率、按课程通过率）")
    @GetMapping("/teacher/dashboard")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<StatsDTO.TeacherDashboard> teacherDashboard(@AuthenticationPrincipal AuthPrincipal principal) {
        if (principal == null || principal.getRefId() == null) {
            return ApiResponse.fail(400, "未关联教师");
        }
        return ApiResponse.ok(statsService.getTeacherDashboard(principal.getRefId()));
    }
}
