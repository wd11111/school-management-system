package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.mark.MarkEnum;
import pl.schoolmanagementsystem.common.mark.dto.AddMarkDto;
import pl.schoolmanagementsystem.common.mark.exception.MarkNotInRangeException;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.teacher.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
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
    public void addMark(String teacherEmail, AddMarkDto addMarkDto, long studentId) {
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
    /*    if (!classRepository.existsById(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        if (!subjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        validateTeacherTeachesSubjectInClass(teacherEmail, subjectName, schoolClassName);
        return studentRepository.findAllInClassWithMarksOfTheSubject(schoolClassName, subjectName);*/

        if (!subjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        SchoolClass schoolClass = classRepository.findClassAndFetchStudentsWithMarks(schoolClassName, subjectName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));

        validateTeacherTeachesSubjectInClass(teacherEmail, subjectName, schoolClassName);
        return schoolClass.getStudents().stream()
                .map(StudentMapper::mapEntityToDtoWithMarks)
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
