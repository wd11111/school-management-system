package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.*;
import pl.schoolmanagementsystem.teacher.dto.GiveMarkDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.utils.MarkMapper;
import pl.schoolmanagementsystem.teacher.utils.StudentMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherProfileService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository subjectRepository;

    private final SchoolClassRepository classRepository;

    private final StudentMapper studentMapper;

    private final MarkMapper markMapper;

    @Transactional
    public void giveMark(String teacherEmail, GiveMarkDto giveMarkDto, Long studentId) {
        BigDecimal mark = giveMarkDto.getMark().value;
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NoSuchStudentException(studentId));
        String schoolClass = student.getSchoolClass();
        SchoolSubject schoolSubject = subjectRepository.findByNameIgnoreCase(giveMarkDto.getSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(giveMarkDto.getSubject()));

        validateTeacherTeachesSubjectInClass(teacherEmail, schoolSubject.getName(), schoolClass);

        student.giveMark(markMapper.mapToEntity(mark, studentId, schoolSubject.getName()));
    }

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(String teacherEmail, Pageable pageable) {
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail, pageable);
    }

    public List<StudentWithMarksDto> getClassStudentsWithMarksOfSubject(String schoolClassName, String subjectName, String teacherEmail) {
        validateSchoolSubjectExists(subjectName);

        SchoolClass schoolClass = classRepository.findClassAndFetchStudentsWithMarks(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));

        validateTeacherTeachesSubjectInClass(teacherEmail, subjectName, schoolClassName);
        filterMarks(schoolClass.getStudents(), subjectName);

        return studentMapper.mapEntitiesToDtosWithMarks(schoolClass.getStudents());
    }

    private void filterMarks(Set<Student> students, String subject) {
        students.forEach(student -> student.setMarks(
                student.getMarks()
                        .stream()
                        .filter(mark -> mark.getSubject().equals(subject))
                        .toList()));
    }

    private void validateSchoolSubjectExists(String subjectName) {
        if (!subjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
    }

    private void validateTeacherTeachesSubjectInClass(String teacherEmail, String subject, String schoolClass) {
        if (!doesTeacherTeachSubjectInClass(teacherEmail, subject, schoolClass)) {
            throw new TeacherDoesNotTeachClassException(subject, schoolClass);
        }
    }

    private boolean doesTeacherTeachSubjectInClass(String teacherEmail, String subject, String schoolClass) {
        return teacherInClassRepository.existsByTeacher_AppUser_EmailAndTaughtSubjectAndTaughtClasses_Name(teacherEmail, subject, schoolClass);
    }
}
