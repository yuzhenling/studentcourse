package com.upper6.studentcourse.service;

import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.TeacherDTO;
import com.upper6.studentcourse.model.entity.Teacher;
import com.upper6.studentcourse.mapper.TeacherMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherMapper teacherMapper;
    private final PasswordEncoder passwordEncoder;

    public Teacher getById(Long id) {
        Teacher t = teacherMapper.selectById(id);
        if (t == null) throw new BusinessException(404, "教师不存在");
        return t;
    }

    public PageResult<Teacher> page(PageParam pageParam, String teacherId, String name, String department, String status) {
        List<Teacher> list = teacherMapper.selectPage(pageParam, teacherId, name, department, status);
        long total = teacherMapper.countPage(teacherId, name, department, status);
        return PageResult.of(list, total, pageParam.getPage(), pageParam.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Teacher create(TeacherDTO dto) {
        if (teacherMapper.selectByTeacherId(dto.getTeacherId()) != null)
            throw new BusinessException(400, "工号已存在");
        Teacher t = new Teacher();
        mapDtoToEntity(dto, t);
        t.setStatus(t.getStatus() != null ? t.getStatus() : "ACTIVE");
        t.setPasswordHash(passwordEncoder.encode("123456"));
        teacherMapper.insert(t);
        return t;
    }

    @Transactional(rollbackFor = Exception.class)
    public Teacher update(Long id, TeacherDTO dto) {
        Teacher existing = getById(id);
        Teacher other = teacherMapper.selectByTeacherId(dto.getTeacherId());
        if (other != null && !other.getId().equals(id)) throw new BusinessException(400, "工号已存在");
        mapDtoToEntity(dto, existing);
        teacherMapper.updateById(existing);
        return existing;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getById(id);
        teacherMapper.deleteById(id);
    }

    private void mapDtoToEntity(TeacherDTO dto, Teacher t) {
        t.setTeacherId(dto.getTeacherId());
        t.setName(dto.getName());
        t.setTitle(dto.getTitle());
        t.setDepartment(dto.getDepartment());
        t.setEmail(dto.getEmail());
        t.setPhone(dto.getPhone());
        t.setOffice(dto.getOffice());
        t.setResearchField(dto.getResearchField());
        if (dto.getStatus() != null) t.setStatus(dto.getStatus());
    }
}
