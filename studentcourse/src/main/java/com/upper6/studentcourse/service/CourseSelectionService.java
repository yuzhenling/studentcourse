package com.upper6.studentcourse.service;

import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.entity.Course;
import com.upper6.studentcourse.model.entity.CourseSelection;
import com.upper6.studentcourse.mapper.CourseMapper;
import com.upper6.studentcourse.mapper.CourseSelectionMapper;
import com.upper6.studentcourse.mapper.SemesterMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 选课服务：选课、退课、查询
 */
@Service
@RequiredArgsConstructor
public class CourseSelectionService {

    private final CourseSelectionMapper selectionMapper;
    private final CourseMapper courseMapper;
    private final SemesterMapper semesterMapper;

    public CourseSelection getById(Long id) {
        CourseSelection cs = selectionMapper.selectById(id);
        if (cs == null) throw new BusinessException(404, "选课记录不存在");
        return cs;
    }

    public List<CourseSelection> listByStudent(Long studentId, String semester) {
        return selectionMapper.selectByStudent(studentId, semester);
    }

    public PageResult<CourseSelection> page(PageParam pageParam, Long studentId, Long courseId, String semester, String status) {
        List<CourseSelection> list = selectionMapper.selectPage(pageParam, studentId, courseId, semester, status);
        long total = selectionMapper.countPage(studentId, courseId, semester, status);
        return PageResult.of(list, total, pageParam.getPage(), pageParam.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public CourseSelection selectCourse(Long studentId, Long courseId, String semester) {
        if (semester == null || semester.isEmpty()) {
            var current = semesterMapper.selectCurrent();
            if (current == null) throw new BusinessException(400, "当前无开放学期");
            semester = current.getSemester();
        }
        if (selectionMapper.selectByStudentAndCourseAndSemester(studentId, courseId, semester) != null)
            throw new BusinessException(400, "已选过该课程");
        Course course = courseMapper.selectById(courseId);
        if (course == null) throw new BusinessException(404, "课程不存在");
        if (!"OPEN".equals(course.getStatus())) throw new BusinessException(400, "课程未开放选课");
        if (course.getCurrentEnrollment() != null && course.getMaxCapacity() != null
                && course.getCurrentEnrollment() >= course.getMaxCapacity())
            throw new BusinessException(400, "课程已满");
        CourseSelection cs = new CourseSelection();
        cs.setStudentId(studentId);
        cs.setCourseId(courseId);
        cs.setSemester(semester);
        cs.setSelectionType("NORMAL");
        cs.setStatus("SELECTED");
        selectionMapper.insert(cs);
        courseMapper.updateCurrentEnrollment(courseId, 1);
        return selectionMapper.selectById(cs.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void withdraw(Long id, Long studentId) {
        CourseSelection cs = getById(id);
        if (!cs.getStudentId().equals(studentId)) throw new BusinessException(403, "只能退自己的选课");
        if (!"SELECTED".equals(cs.getStatus())) throw new BusinessException(400, "当前状态不可退课");
        selectionMapper.updateStatus(id, "WITHDRAWN");
        courseMapper.updateCurrentEnrollment(cs.getCourseId(), -1);
    }
}
