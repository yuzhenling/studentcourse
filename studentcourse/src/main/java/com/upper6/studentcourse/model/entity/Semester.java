package com.upper6.studentcourse.model.entity;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Semester {

    private Long id;
    private String semester;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Instant selectionStart;
    private Instant selectionEnd;
    private Boolean isCurrent;
    private Instant createdAt;
    private Instant updatedAt;
}
