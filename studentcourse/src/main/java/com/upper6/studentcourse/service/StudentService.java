package com.upper6.studentcourse.service;

import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.StudentDTO;
import com.upper6.studentcourse.model.entity.Student;
import com.upper6.studentcourse.mapper.StudentMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生管理服务
 */
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    public Student getById(Long id) {
        Student s = studentMapper.selectById(id);
        if (s == null) {
            throw new BusinessException(404, "学生不存在");
        }
        return s;
    }

    public PageResult<Student> page(PageParam pageParam, String studentId, String name, String major, String status) {
        List<Student> list = studentMapper.selectPage(pageParam, studentId, name, major, status);
        long total = studentMapper.countPage(studentId, name, major, status);
        return PageResult.of(list, total, pageParam.getPage(), pageParam.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Student create(StudentDTO dto) {
        if (studentMapper.selectByStudentId(dto.getStudentId()) != null) {
            throw new BusinessException(400, "学号已存在");
        }
        Student s = new Student();
        mapDtoToEntity(dto, s);
        s.setStatus(s.getStatus() != null ? s.getStatus() : "ACTIVE");
        s.setPasswordHash(passwordEncoder.encode("123456"));
        studentMapper.insert(s);
        return s;
    }

    @Transactional(rollbackFor = Exception.class)
    public Student update(Long id, StudentDTO dto) {
        Student existing = getById(id);
        Student other = studentMapper.selectByStudentId(dto.getStudentId());
        if (other != null && !other.getId().equals(id)) {
            throw new BusinessException(400, "学号已存在");
        }
        mapDtoToEntity(dto, existing);
        studentMapper.updateById(existing);
        return existing;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getById(id);
        studentMapper.deleteById(id);
    }

    private void mapDtoToEntity(StudentDTO dto, Student s) {
        s.setStudentId(dto.getStudentId());
        s.setName(dto.getName());
        s.setGender(dto.getGender());
        s.setBirthDate(dto.getBirthDate());
        s.setEmail(dto.getEmail());
        s.setPhone(dto.getPhone());
        s.setMajor(dto.getMajor());
        s.setEnrollmentYear(dto.getEnrollmentYear());
        s.setClassId(dto.getClassId());
        if (dto.getStatus() != null) {
            s.setStatus(dto.getStatus());
        }
    }
}
