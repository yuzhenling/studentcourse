package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.config.security.AuthPrincipal;
import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.GradeDTO;
import com.upper6.studentcourse.model.entity.Grade;
import com.upper6.studentcourse.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "成绩管理")
@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @Operation(summary = "我的成绩（当前登录学生，仅已发布）")
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ApiResponse<List<Grade>> myGrades(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) String semester) {
        if (principal.getRefId() == null) return ApiResponse.fail(400, "当前用户未关联学生");
        return ApiResponse.ok(gradeService.listByStudent(principal.getRefId(), semester));
    }

    @Operation(summary = "分页查询成绩（教师/管理员）")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<PageResult<Grade>> page(
            PageParam pageParam,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) Boolean published) {
        return ApiResponse.ok(gradeService.page(pageParam, studentId, courseId, semester, published));
    }

    @Operation(summary = "根据 ID 查询成绩")
    @GetMapping("/{id}")
    public ApiResponse<Grade> getById(@PathVariable Long id) {
        return ApiResponse.ok(gradeService.getById(id));
    }

    @Operation(summary = "录入/更新成绩（教师/管理员）")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<Grade> save(@Valid @RequestBody GradeDTO dto) {
        return ApiResponse.ok(gradeService.save(dto));
    }

    @Operation(summary = "发布/撤销成绩")
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<Void> publish(@PathVariable Long id, @RequestParam boolean published) {
        gradeService.publish(id, published);
        return ApiResponse.ok(null);
    }
}
