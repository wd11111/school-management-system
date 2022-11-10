package pl.schoolmanagementsystem.schoolclass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.mapper.StudentMapper;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.schoolclass.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.schoolsubject.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.student.repository.StudentRepository;
import pl.schoolmanagementsystem.teacher.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final StudentRepository studentRepository;

    private final TeacherService teacherService;

    private final TeacherRepository teacherRepository;

    public SchoolClassDto createSchoolClass(SchoolClassDto schoolClassDto) {
        checkIfClassAlreadyExists(schoolClassDto);
        schoolClassRepository.save(buildSchoolClass(schoolClassDto));
        return schoolClassDto;
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String schoolClassName) {
        checkIfClassExists(schoolClassName);
        return studentRepository.findAllStudentsInClass(schoolClassName);
    }

    public List<SchoolClassDto> getListOfClasses() {
        return schoolClassRepository.findAllSchoolClasses();
    }

    public List<SubjectAndTeacherOutputDto> getAllSubjectsForSchoolClass(String schoolClassName) {
        checkIfClassExists(schoolClassName);
        return schoolClassRepository.findAllSubjectsForSchoolClass(schoolClassName);
    }

    public List<StudentOutputDto3> getAllStudentsInClassWithMarksOfTheSubject(String schoolClassName, String subjectName, String teacherEmail) {
        SchoolSubject subject = findSubject(subjectName);
        SchoolClass schoolClass = findSchoolClass(schoolClassName);
        Teacher teacher = findTeacherByEmail(teacherEmail);
        checkIfTeacherTeachesThisClass(teacher, subject, schoolClass);
        return getStudentsWithIntegerMarks(schoolClassName, subjectName);
    }

    private List<StudentOutputDto3> getStudentsWithIntegerMarks(String schoolClassName, String subjectName) {
        List<Student> students = studentRepository.findAllStudentsInClassWithMarksOfTheSubject(schoolClassName, subjectName);
        return students.stream()
                .map(StudentMapper::mapStudentToOutputDto3)
                .collect(Collectors.toList());
    }

    private SchoolSubject findSubject(String subjectName) {
        return schoolSubjectRepository.findBySubjectName(subjectName)
                .orElseThrow(() -> new NoSuchSchoolSubjectException(subjectName));
    }

    private Teacher findTeacherByEmail(String email) {
        return teacherRepository.findByEmail_Email(email).get();
    }

    public SchoolClass findSchoolClass(String schoolClassName) {
        return schoolClassRepository.findBySchoolClassName(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
    }

    private void checkIfTeacherTeachesThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        teacherService.checkIfTeacherTeachesThisClass(teacher, schoolSubject, schoolClass);
    }

    private SchoolClass buildSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }

    private void checkIfClassExists(String schoolClassName) {
        if (!doesSchoolClassExistsByName(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
    }

    private void checkIfClassAlreadyExists(SchoolClassDto schoolClassDto) {
        if (doesSchoolClassExistsByName(schoolClassDto.getSchoolClassName())) {
            throw new ClassAlreadyExistsException(schoolClassDto);
        }
    }

    private boolean doesSchoolClassExistsByName(String schoolClassName) {
        return schoolClassRepository.existsByName(schoolClassName);
    }
}
