package com.upper6.studentcourse.model.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 班级实体（类名用 ClassEntity 避免与 Java 关键字 class 冲突）
 */
@Data
public class ClassEntity {

    private Long id;
    private String className;
    private String major;
    private Integer enrollmentYear;
    private Instant createdAt;
    private Instant updatedAt;
}
