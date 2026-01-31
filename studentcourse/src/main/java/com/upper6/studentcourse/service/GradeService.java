package com.upper6.studentcourse.service;

import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.common.PageResult;
import com.upper6.studentcourse.model.dto.GradeDTO;
import com.upper6.studentcourse.model.entity.CourseSelection;
import com.upper6.studentcourse.model.entity.Grade;
import com.upper6.studentcourse.mapper.CourseSelectionMapper;
import com.upper6.studentcourse.mapper.GradeMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeMapper gradeMapper;
    private final CourseSelectionMapper selectionMapper;

    public Grade getById(Long id) {
        Grade g = gradeMapper.selectById(id);
        if (g == null) throw new BusinessException(404, "成绩记录不存在");
        return g;
    }

    public List<Grade> listByStudent(Long studentId, String semester) {
        return gradeMapper.selectByStudent(studentId, semester);
    }

    public PageResult<Grade> page(PageParam pageParam, Long studentId, Long courseId, String semester, Boolean published) {
        List<Grade> list = gradeMapper.selectPage(pageParam, studentId, courseId, semester, published);
        long total = gradeMapper.countPage(studentId, courseId, semester, published);
        return PageResult.of(list, total, pageParam.getPage(), pageParam.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Grade save(GradeDTO dto) {
        CourseSelection cs = selectionMapper.selectById(dto.getSelectionId());
        if (cs == null) throw new BusinessException(404, "选课记录不存在");
        Grade existing = gradeMapper.selectBySelectionId(dto.getSelectionId());
        if (existing != null) {
            mapDtoToEntity(dto, existing);
            computeTotal(existing);
            gradeMapper.updateById(existing);
            return gradeMapper.selectById(existing.getId());
        }
        Grade g = new Grade();
        g.setSelectionId(dto.getSelectionId());
        mapDtoToEntity(dto, g);
        computeTotal(g);
        gradeMapper.insert(g);
        return gradeMapper.selectById(g.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id, boolean published) {
        getById(id);
        gradeMapper.updatePublished(id, published);
    }

    private void mapDtoToEntity(GradeDTO dto, Grade g) {
        g.setRegularScore(dto.getRegularScore());
        g.setMidtermScore(dto.getMidtermScore());
        g.setFinalScore(dto.getFinalScore());
        g.setTotalScore(dto.getTotalScore());
        g.setGradePoint(dto.getGradePoint());
        g.setLetterGrade(dto.getLetterGrade());
        g.setIsPassed(dto.getIsPassed());
        g.setTeacherComment(dto.getTeacherComment());
    }

    private void computeTotal(Grade g) {
        if (g.getTotalScore() != null) return;
        BigDecimal r = g.getRegularScore() != null ? g.getRegularScore() : BigDecimal.ZERO;
        BigDecimal m = g.getMidtermScore() != null ? g.getMidtermScore() : BigDecimal.ZERO;
        BigDecimal f = g.getFinalScore() != null ? g.getFinalScore() : BigDecimal.ZERO;
        // 简单权重：平时20% 期中30% 期末50%
        BigDecimal total = r.multiply(BigDecimal.valueOf(0.2))
                .add(m.multiply(BigDecimal.valueOf(0.3)))
                .add(f.multiply(BigDecimal.valueOf(0.5)));
        g.setTotalScore(total.setScale(2, RoundingMode.HALF_UP));
        if (g.getIsPassed() == null) g.setIsPassed(total.compareTo(BigDecimal.valueOf(60)) >= 0);
    }
}
