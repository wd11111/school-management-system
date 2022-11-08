package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.mapper.StudentMapper;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto2;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto3;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.repository.TeacherRepository;

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

    private SchoolClass findSchoolClass(String schoolClassName) {
        return schoolClassRepository.findBySchoolClassName(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
    }

    private void checkIfTeacherTeachesThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        teacherService.checkIfTeacherTeachesThisClass(teacher, schoolSubject, schoolClass);
    }

    private SchoolClass buildSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .schoolClassName(schoolClassDto.getSchoolClassName())
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
        return schoolClassRepository.existsBySchoolClassName(schoolClassName);
    }
}
