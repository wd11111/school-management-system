package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.model.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.repository.TeacherRepository;
import pl.schoolmanagementsystem.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.mapper.MarkMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherClassService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository subjectRepository;

    private final SchoolClassRepository classRepository;

    @Transactional
    public void addMark(String teacherEmail, MarkDto markDto, long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NoSuchStudentException(studentId));
        String schoolClass = student.getSchoolClass();

        SchoolSubject schoolSubject = subjectRepository.findByNameIgnoreCase(markDto.getSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(markDto.getSubject()));

        validateTeacherTeachesSubjectInClass(teacherEmail, schoolSubject.getName(), schoolClass);
        student.addMark(MarkMapper.createMarkEntity(markDto, studentId));
    }
    // todo warto takie rzeczy dzielić np. tak:
//    public void addMark(String teacherEmail, MarkDto markDto, long studentId) {
//        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NoSuchStudentException(studentId));
//        String schoolClass = student.getSchoolClass();

//        SchoolSubject schoolSubject = subjectRepository
//                .findByNameIgnoreCase(markDto.getSubject())
//                .orElseThrow(() -> new NoSuchSchoolSubjectException(markDto.getSubject()));

//        validateTeacherTeachesSubjectInClass(teacherEmail, schoolSubject.getName(), schoolClass);
//        student.addMark(MarkMapper.createMarkEntity(markDto, studentId));
//    }
    //todo dzięki temu mi przynajmniej się to lepiej czyta, wiem, że to mało istotne, ale dla osób które czytają Twój kod to może być istotne

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(String teacherEmail, Pageable pageable) {
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail, PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize()));
    }

    public List<Student> getClassStudentsWithMarksOfSubject(String schoolClassName, String subjectName, String teacherEmail) {
        if (!classRepository.existsById(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        if (!subjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        validateTeacherTeachesSubjectInClass(teacherEmail, subjectName, schoolClassName);
        return studentRepository.findAllInClassWithMarksOfTheSubject(schoolClassName, subjectName);
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
