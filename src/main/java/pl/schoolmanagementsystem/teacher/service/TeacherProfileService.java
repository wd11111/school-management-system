package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.exception.*;
import pl.schoolmanagementsystem.common.model.*;
import pl.schoolmanagementsystem.common.repository.*;
import pl.schoolmanagementsystem.teacher.dto.AddMarkDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.utils.StudentMapper;

import java.math.BigDecimal;
import java.util.List;

import static pl.schoolmanagementsystem.teacher.utils.MarkMapper.mapDtoToEntity;

@Service
@RequiredArgsConstructor
public class TeacherProfileService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository subjectRepository;

    private final SchoolClassRepository classRepository;

    @Transactional
    public void addMark(String teacherEmail, AddMarkDto addMarkDto, Long studentId) {
        BigDecimal mark = MarkEnum.getValueByName(addMarkDto.getMark()).orElseThrow(MarkNotInRangeException::new);

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NoSuchStudentException(studentId));
        String schoolClass = student.getSchoolClass();
        SchoolSubject schoolSubject = subjectRepository.findByNameIgnoreCase(addMarkDto.getSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(addMarkDto.getSubject()));

        validateTeacherTeachesSubjectInClass(teacherEmail, schoolSubject.getName(), schoolClass);
        student.addMark(mapDtoToEntity(mark, studentId, schoolSubject.getName()));
    }

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(String teacherEmail, Pageable pageable) {
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail, pageable);
    }

    public List<StudentWithMarksDto> getClassStudentsWithMarksOfSubject(String schoolClassName, String subjectName, String teacherEmail) {
        if (!subjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        SchoolClass schoolClass = classRepository.findClassAndFetchStudentsWithMarks(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
        validateTeacherTeachesSubjectInClass(teacherEmail, subjectName, schoolClassName);

        schoolClass.getStudents()
                .forEach(student -> student.setMarks(filterMarks(student.getMarks(), subjectName)));

        return schoolClass.getStudents().stream()
                .map(StudentMapper::mapEntityToDtoWithMarks)
                .toList();
    }

    private List<Mark> filterMarks(List<Mark> marks, String subject) {
        return marks.stream()
                .filter(mark -> mark.getSubject().equals(subject))
                .toList();
    }

    private void validateTeacherTeachesSubjectInClass(String teacherEmail, String subject, String schoolClass) {
        if (!doesTeacherTeachSubjectInClass(teacherEmail, subject, schoolClass)) {
            throw new TeacherDoesNotTeachClassException(subject, schoolClass);
        }
    }

    private boolean doesTeacherTeachSubjectInClass(String teacherEmail, String subject, String schoolClass) {
        return teacherInClassRepository.existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(teacherEmail, subject, schoolClass);
    }
}
