package com.upper6.studentcourse.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import lombok.Data;

/**
 * 教师实体
 */
@Data
public class Teacher {

    private Long id;
    private String teacherId;
    private String name;
    private String title;
    private String department;
    private String email;
    private String phone;
    private String office;
    private String researchField;
    @JsonIgnore
    private String passwordHash;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
