package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.entity.CourseSelection;
import com.upper6.studentcourse.config.security.AuthPrincipal;
import com.upper6.studentcourse.service.CourseSelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "选课管理")
@RestController
@RequestMapping("/selections")
@RequiredArgsConstructor
public class CourseSelectionController {

    private final CourseSelectionService selectionService;

    @Operation(summary = "我的选课列表（当前登录学生）")
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ApiResponse<List<CourseSelection>> mySelections(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) String semester) {
        if (principal.getRefId() == null) return ApiResponse.fail(400, "当前用户未关联学生");
        return ApiResponse.ok(selectionService.listByStudent(principal.getRefId(), semester));
    }

    @Operation(summary = "分页查询选课记录（管理员/教师）")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<PageResult<CourseSelection>> page(
            PageParam pageParam,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String status) {
        return ApiResponse.ok(selectionService.page(pageParam, studentId, courseId, semester, status));
    }

    @Operation(summary = "选课（学生，courseId 必填，semester 可选）")
    @PostMapping("/select")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ApiResponse<CourseSelection> select(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody Map<String, Object> body) {
        if (principal.getRefId() == null) return ApiResponse.fail(400, "当前用户未关联学生");
        Long courseId = body.get("courseId") != null ? Long.valueOf(body.get("courseId").toString()) : null;
        String semester = body.get("semester") != null ? body.get("semester").toString() : null;
        if (courseId == null) return ApiResponse.fail(400, "缺少 courseId");
        return ApiResponse.ok(selectionService.selectCourse(principal.getRefId(), courseId, semester));
    }

    @Operation(summary = "退课（学生）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ApiResponse<Void> withdraw(@PathVariable Long id, @AuthenticationPrincipal AuthPrincipal principal) {
        if (principal.getRefId() == null) return ApiResponse.fail(400, "当前用户未关联学生");
        selectionService.withdraw(id, principal.getRefId());
        return ApiResponse.ok(null);
    }

    @Operation(summary = "按学生 ID 查选课列表（供前端我的选课页）")
    @GetMapping("/student/{studentId}")
    public ApiResponse<List<CourseSelection>> listByStudent(@PathVariable Long studentId, @RequestParam(required = false) String semester) {
        return ApiResponse.ok(selectionService.listByStudent(studentId, semester));
    }
}
