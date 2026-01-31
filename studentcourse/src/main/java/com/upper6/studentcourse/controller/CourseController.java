package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.CourseDTO;
import com.upper6.studentcourse.model.entity.Course;
import com.upper6.studentcourse.config.security.AuthPrincipal;
import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "课程管理")
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "分页查询课程（可选学期、状态；教师登录时仅返回本人课程）")
    @GetMapping
    public ApiResponse<PageResult<Course>> page(
            PageParam pageParam,
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal AuthPrincipal principal) {
        // 教师只能查看自己开设的课程，忽略请求中的 teacherId
        if (principal != null && "TEACHER".equals(principal.getRole())) {
            teacherId = principal.getRefId();
        }
        return ApiResponse.ok(courseService.page(pageParam, courseCode, courseName, semester, teacherId, status));
    }

    @Operation(summary = "根据 ID 查询课程")
    @GetMapping("/{id}")
    public ApiResponse<Course> getById(@PathVariable Long id) {
        return ApiResponse.ok(courseService.getById(id));
    }

    @Operation(summary = "新增课程（教师/管理员）")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<Course> create(@Valid @RequestBody CourseDTO dto) {
        return ApiResponse.ok(courseService.create(dto));
    }

    @Operation(summary = "更新课程（教师/管理员）")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<Course> update(@PathVariable Long id, @Valid @RequestBody CourseDTO dto) {
        return ApiResponse.ok(courseService.update(id, dto));
    }

    @Operation(summary = "删除课程（管理员/教师，教师仅能删自己的课程）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<Void> delete(@PathVariable Long id, @AuthenticationPrincipal AuthPrincipal principal) {
        if (principal != null && "TEACHER".equals(principal.getRole())) {
            Course c = courseService.getById(id);
            if (c.getTeacherId() == null || !c.getTeacherId().equals(principal.getRefId())) {
                throw new BusinessException(403, "只能删除自己开设的课程");
            }
        }
        courseService.delete(id);
        return ApiResponse.ok(null);
    }
}
