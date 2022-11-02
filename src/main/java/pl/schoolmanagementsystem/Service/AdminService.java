package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Model.*;
import pl.schoolmanagementsystem.Model.dto.CreateStudentDto;
import pl.schoolmanagementsystem.Model.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.Model.dto.Name;
import pl.schoolmanagementsystem.Model.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.Repository.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolClass createSchoolClass(Name schoolClassName) {
        if (doesSchoolClassAlreadyExistsInDatabase(schoolClassName.getPlainText())) {
            throw new RuntimeException();
        }
        return schoolClassRepository.save(SchoolClass.builder()
                .schoolClassName(schoolClassName.getPlainText())
                .build());
    }

    public Student createStudent(CreateStudentDto createStudentDto) {
        SchoolClass schoolClass = getSchoolClassByName(createStudentDto.getSchoolClassName()).orElseThrow();
        return studentRepository.save(Student.builder()
                .name(createStudentDto.getName())
                .surname(createStudentDto.getSurname())
                .schoolClass(schoolClass)
                .build());
    }

    public Teacher createTeacher(CreateTeacherDto createTeacherDto) {
        Teacher teacher = new Teacher();
        teacher.setName(createTeacherDto.getName());
        teacher.setSurname(createTeacherDto.getSurname());
        addTaughtSubjectsToTeacher(teacher, createTeacherDto.getTaughtSubjects());
        return teacherRepository.save(teacher);
    }

    public TeacherInClass addExistingTeacherToClass(TeacherInClassDto teacherInClassDto) {
        Teacher teacher = getTeacherById(teacherInClassDto.getTeacherId())
                .orElseThrow();
        SchoolSubject schoolSubject = getSchoolSubjectByName(teacherInClassDto.getTaughtSubject())
                .orElseThrow();
        SchoolClass schoolClass = getSchoolClassByName(teacherInClassDto.getSchoolClassName())
                .orElseThrow();
        if (!doesTeacherTeachTheSubject(teacher, schoolSubject)) {
            throw new RuntimeException();
        }
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElse(new TeacherInClass());
        teacherInClass.setTeacher(teacher);
        teacherInClass.setTaughtSubject(schoolSubject);
        teacherInClass.getTaughtClasses().add(schoolClass);
        return teacherInClassRepository.save(teacherInClass);
    }

    public SchoolSubject addSchoolSubject(Name subjectName) {
        if (doesSubjectAlreadyExistsInDatabase(subjectName.getPlainText())) {
            throw new RuntimeException();
        }
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setSubjectName(subjectName.getPlainText());
        return schoolSubjectRepository.save(schoolSubject);
    }

    private void addTaughtSubjectsToTeacher(Teacher teacher, Set<String> subjects) {
        Set<SchoolSubject> subjectObjects = transformSetOfStringsToSetOfSchoolClassObjects(subjects);
        subjectObjects.forEach(subject -> teacher.getTaughtSubjects().add(subject));
    }

    private Set<SchoolSubject> transformSetOfStringsToSetOfSchoolClassObjects(Set<String> subjects) {
        return subjects.stream()
                .filter(subject -> doesSubjectAlreadyExistsInDatabase(subject))
                .map(subject -> getSchoolSubjectByName(subject).orElse(null))
                .collect(Collectors.toSet());
    }

    private boolean doesTeacherTeachTheSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher, SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private boolean doesSubjectAlreadyExistsInDatabase(String subjectName) {
        return schoolSubjectRepository.existsBySubjectName(subjectName);
    }

    private boolean doesSchoolClassAlreadyExistsInDatabase(String schoolClassName) {
        return schoolClassRepository.existsBySchoolClassName(schoolClassName);
    }

    private Optional<SchoolClass> getSchoolClassByName(String schoolClassName) {
        return schoolClassRepository.findBySchoolClassName(schoolClassName);
    }

    private Optional<SchoolSubject> getSchoolSubjectByName(String schoolSubjectName) {
        return schoolSubjectRepository.findBySubjectName(schoolSubjectName);
    }

    private Optional<Teacher> getTeacherById(int id) {
        return teacherRepository.findById(id);
    }
}
