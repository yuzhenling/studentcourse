package com.upper6.studentcourse.service;

import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.CourseDTO;
import com.upper6.studentcourse.model.entity.Course;
import com.upper6.studentcourse.mapper.CourseMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseMapper courseMapper;

    public Course getById(Long id) {
        Course c = courseMapper.selectById(id);
        if (c == null) throw new BusinessException(404, "课程不存在");
        return c;
    }

    public PageResult<Course> page(PageParam pageParam, String courseCode, String courseName, String semester, Long teacherId, String status) {
        List<Course> list = courseMapper.selectPage(pageParam, courseCode, courseName, semester, teacherId, status);
        long total = courseMapper.countPage(courseCode, courseName, semester, teacherId, status);
        return PageResult.of(list, total, pageParam.getPage(), pageParam.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Course create(CourseDTO dto) {
        if (courseMapper.selectByCourseCode(dto.getCourseCode(), dto.getSemester()) != null)
            throw new BusinessException(400, "该学期课程代码已存在");
        Course c = new Course();
        mapDtoToEntity(dto, c);
        c.setCurrentEnrollment(0);
        c.setStatus(c.getStatus() != null ? c.getStatus() : "OPEN");
        if (c.getMaxCapacity() == null) c.setMaxCapacity(100);
        courseMapper.insert(c);
        return c;
    }

    @Transactional(rollbackFor = Exception.class)
    public Course update(Long id, CourseDTO dto) {
        Course existing = getById(id);
        Course other = courseMapper.selectByCourseCode(dto.getCourseCode(), dto.getSemester());
        if (other != null && !other.getId().equals(id)) throw new BusinessException(400, "该学期课程代码已存在");
        mapDtoToEntity(dto, existing);
        courseMapper.updateById(existing);
        return existing;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getById(id);
        courseMapper.deleteById(id);
    }

    private void mapDtoToEntity(CourseDTO dto, Course c) {
        c.setCourseCode(dto.getCourseCode());
        c.setCourseName(dto.getCourseName());
        c.setCredits(dto.getCredits());
        c.setCourseHours(dto.getCourseHours());
        c.setCourseType(dto.getCourseType());
        c.setDescription(dto.getDescription());
        c.setMaxCapacity(dto.getMaxCapacity());
        c.setSemester(dto.getSemester());
        c.setYear(dto.getYear());
        c.setTeacherId(dto.getTeacherId());
        c.setScheduleInfo(dto.getScheduleInfo());
        if (dto.getStatus() != null) c.setStatus(dto.getStatus());
    }
}
