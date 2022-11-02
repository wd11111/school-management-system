package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Model.*;
import pl.schoolmanagementsystem.Model.dto.CreateStudentDto;
import pl.schoolmanagementsystem.Model.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.Model.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.Repository.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolClass createSchoolClass(String schoolClassName) {
        if (doesSchoolClassAlreadyExistsInDatabase(schoolClassName)) {
            throw new RuntimeException();
        }
        return schoolClassRepository.save(SchoolClass.builder()
                .schoolClassName(schoolClassName)
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
        teacher.getTaughtSubjects().addAll(createTeacherDto.getTaughtSubjects());
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
                .orElse(TeacherInClass.builder()
                        .teacher(teacher)
                        .taughtSubject(schoolSubject)
                        .build());
        teacherInClass.getTaughtClasses().add(schoolClass);
        return teacherInClassRepository.save(teacherInClass);
    }

    public SchoolSubject addSchoolSubject(String subjectName) {
        if (doesSubjectAlreadyExistsInDatabase(subjectName)) {
            throw new RuntimeException();
        }
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setSubjectName(subjectName);
        return schoolSubjectRepository.save(schoolSubject);
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
