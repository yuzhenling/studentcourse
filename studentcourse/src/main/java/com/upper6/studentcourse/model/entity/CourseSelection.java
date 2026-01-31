package com.upper6.studentcourse.model.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 选课记录实体
 */
@Data
public class CourseSelection {

    private Long id;
    private Long studentId;
    private Long courseId;
    private String semester;
    private Instant selectionDate;
    private String selectionType;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    private String studentName;
    private String courseName;
    private String courseCode;
}
