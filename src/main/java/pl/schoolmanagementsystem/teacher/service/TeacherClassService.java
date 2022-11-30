package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.mark.dto.MarkInputDto;
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

import java.util.List;

import static pl.schoolmanagementsystem.common.mark.MarkMapper.createMark;

@Service
@RequiredArgsConstructor
public class TeacherClassService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository subjectRepository;

    private final SchoolClassRepository classRepository;

    @Transactional
    public void addMark(String teacherEmail, MarkInputDto markInputDto, long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NoSuchStudentException(studentId));
        String schoolClass = student.getSchoolClass();
        SchoolSubject schoolSubject = subjectRepository.findByNameIgnoreCase(markInputDto.getSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(markInputDto.getSubject()));
        checkIfTeacherTeachesSubjectInClass(teacherEmail, schoolSubject.getName(), schoolClass);
        student.addMark(createMark(markInputDto, studentId));
    }

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(String teacherEmail, Pageable pageable) {
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail, PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(), Sort.by("schoolClass").descending()));
    }

    public List<Student> getClassStudentsWithMarksOfSubject(String schoolClassName, String subjectName, String teacherEmail) {
        if (!classRepository.existsById(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        if (!subjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        checkIfTeacherTeachesSubjectInClass(teacherEmail, subjectName, schoolClassName);
        return studentRepository.findAllInClassWithMarksOfTheSubject(schoolClassName, subjectName);
    }

    private void checkIfTeacherTeachesSubjectInClass(String teacherEmail, String subject, String schoolClass) {
        if (!doesTeacherTeachSubjectInClass(teacherEmail, subject, schoolClass)) {
            throw new TeacherDoesNotTeachClassException(subject, schoolClass);
        }
    }

    private boolean doesTeacherTeachSubjectInClass(String teacherEmail, String subject, String schoolClass) {
        return teacherInClassRepository.existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(teacherEmail, subject, schoolClass);
    }
}
