package com.upper6.studentcourse.controller;

import com.upper6.studentcourse.model.common.ApiResponse;
import com.upper6.studentcourse.model.entity.Semester;
import com.upper6.studentcourse.mapper.SemesterMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/semesters")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterMapper semesterMapper;

    @GetMapping
    public ApiResponse<List<Semester>> list() {
        return ApiResponse.ok(semesterMapper.selectAll());
    }

    @GetMapping("/current")
    public ApiResponse<Semester> current() {
        return ApiResponse.ok(semesterMapper.selectCurrent());
    }
}
