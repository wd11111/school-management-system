package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
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
    public void addMark(String teacherEmail, MarkInputDto markInputDto, int studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NoSuchStudentException(studentId));
        String schoolClass = student.getSchoolClass();
        SchoolSubject schoolSubject = subjectRepository.findByNameIgnoreCase(markInputDto.getSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(markInputDto.getSubject()));
        checkIfTeacherTeachesSubjectInClass(teacherEmail, schoolSubject.getName(), schoolClass);
        student.addMark(createMark(markInputDto, studentId));
    }

    public List<SubjectAndClassDto> getTaughtClassesByTeacher(String teacherEmail) {
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail);
    }

    public List<Student> getAllStudentsInClassWithMarksOfSubject(String schoolClassName, String subjectName, String teacherEmail) {
        if (!classRepository.existsById(schoolClassName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        if (!subjectRepository.existsByName(subjectName)) {
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
        return teacherInClassRepository.existsByTeacher_Email_EmailAndTaughtSubjectAndTaughtClasses_Name(teacherEmail, subject, schoolClass);
    }
}
