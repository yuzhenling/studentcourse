package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.entity.ClassEntity;
import com.upper6.studentcourse.mapper.ClassMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 班级接口（供下拉选择等）
 */
@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassMapper classMapper;

    @GetMapping
    public ApiResponse<List<ClassEntity>> list() {
        return ApiResponse.ok(classMapper.selectAll());
    }
}
