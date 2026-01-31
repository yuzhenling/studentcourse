package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.TeacherDTO;
import com.upper6.studentcourse.model.entity.Teacher;
import com.upper6.studentcourse.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "教师管理")
@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "分页查询教师")
    @GetMapping
    public ApiResponse<PageResult<Teacher>> page(
            PageParam pageParam,
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status) {
        return ApiResponse.ok(teacherService.page(pageParam, teacherId, name, department, status));
    }

    @Operation(summary = "根据 ID 查询教师")
    @GetMapping("/{id}")
    public ApiResponse<Teacher> getById(@PathVariable Long id) {
        return ApiResponse.ok(teacherService.getById(id));
    }

    @Operation(summary = "新增教师")
    @PostMapping
    public ApiResponse<Teacher> create(@Valid @RequestBody TeacherDTO dto) {
        return ApiResponse.ok(teacherService.create(dto));
    }

    @Operation(summary = "更新教师")
    @PutMapping("/{id}")
    public ApiResponse<Teacher> update(@PathVariable Long id, @Valid @RequestBody TeacherDTO dto) {
        return ApiResponse.ok(teacherService.update(id, dto));
    }

    @Operation(summary = "删除教师")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ApiResponse.ok(null);
    }
}
