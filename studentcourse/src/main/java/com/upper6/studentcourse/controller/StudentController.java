package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.StudentDTO;
import com.upper6.studentcourse.model.entity.Student;
import com.upper6.studentcourse.service.StudentService;
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

/**
 * 学生管理接口
 */
@Tag(name = "学生管理")
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "分页查询学生")
    @GetMapping
    public ApiResponse<PageResult<Student>> page(
            PageParam pageParam,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String status) {
        PageResult<Student> result = studentService.page(pageParam, studentId, name, major, status);
        return ApiResponse.ok(result);
    }

    @Operation(summary = "根据 ID 查询学生")
    @GetMapping("/{id}")
    public ApiResponse<Student> getById(@PathVariable Long id) {
        return ApiResponse.ok(studentService.getById(id));
    }

    @Operation(summary = "新增学生")
    @PostMapping
    public ApiResponse<Student> create(@Valid @RequestBody StudentDTO dto) {
        return ApiResponse.ok(studentService.create(dto));
    }

    @Operation(summary = "更新学生")
    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
        return ApiResponse.ok(studentService.update(id, dto));
    }

    @Operation(summary = "删除学生")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ApiResponse.ok(null);
    }
}
