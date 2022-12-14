package pl.schoolmanagementsystem.teacher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherProfileServiceTest implements Samples {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherInClassRepository teacherInClassRepository;

    @Mock
    private SchoolSubjectRepository subjectRepository;

    @Mock
    private SchoolClassRepository classRepository;

    @InjectMocks
    private TeacherProfileService teacherProfileService;

    @Test
    void should_return_taught_classes_by_teacher() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SubjectAndClassDto> expectedList = new PageImpl<>(listOfTaughtClasses());
        when(teacherRepository.findTaughtClassesByTeacher(anyString(), any())).thenReturn(expectedList);

        Page<SubjectAndClassDto> result = teacherProfileService.getTaughtClassesByTeacher(NAME2, pageable);

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void should_correctly_add_mark_to_student() {
        Student student = createStudent();
        SchoolSubject schoolSubject = createSchoolSubject();
        MarkDto markDto = new MarkDto((byte) 2, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(student));
        when(subjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(schoolSubject));
        when(teacherInClassRepository
                .existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(true);

        teacherProfileService.addMark(NAME2, markDto, student.getId());

        assertThat(student.getMarks()).hasSize(1);
    }

    @Test
    void should_throw_exception_when_student_not_found() {
        MarkDto markDto = new MarkDto((byte) 2, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherProfileService.addMark(NAME2, markDto, ID_1))
                .isInstanceOf(NoSuchStudentException.class)
                .hasMessage("Student with such an id does not exist: " + ID_1);
    }

    @Test
    void should_throw_exception_when_school_subject_is_incorrect() {
        Student student = createStudent();
        MarkDto markDto = new MarkDto((byte) 2, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(student));
        when(subjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherProfileService.addMark(NAME2, markDto, ID_1))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + markDto.getSubject());
        assertThat(student.getMarks()).hasSize(0);
    }

    @Test
    void should_throw_exception_when_teacher_does_not_teach_subject_in_class() {
        Student student = createStudent();
        SchoolSubject schoolSubject = createSchoolSubject();
        MarkDto markDto = new MarkDto((byte) 2, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(subjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherInClassRepository
                .existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.addMark(NAME2, markDto, ID_1))
                .isInstanceOf(TeacherDoesNotTeachClassException.class)
                .hasMessage("You do not teach Biology in 1a");
        assertThat(student.getMarks()).hasSize(0);
    }

    @Test
    void should_return_all_students_in_class_with_marks_of_subject() {
        List<Student> listOfStudents = List.of(createStudent(), createStudent2());
        when(classRepository.existsById(anyString())).thenReturn(true);
        when(subjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherInClassRepository
                .existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(true);
        when(studentRepository.findAllInClassWithMarksOfTheSubject(any(), any())).thenReturn(listOfStudents);

        List<Student> result = teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2);

        assertThat(result).containsAll(listOfStudents);
    }

    @Test
    void should_throw_exception_when_school_class_doesnt_exist() {
        when(classRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + CLASS_1A);
    }

    @Test
    void should_throw_exception_when_school_subject_doesnt_exist() {
        when(classRepository.existsById(anyString())).thenReturn(true);
        when(subjectRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + SUBJECT_BIOLOGY);
    }

    @Test
    void should_throw_exception_when_teacher_does_not_teach_subject_in_class_while_getting_students() {
        when(classRepository.existsById(anyString())).thenReturn(true);
        when(subjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherInClassRepository
                .existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2))
                .isInstanceOf(TeacherDoesNotTeachClassException.class)
                .hasMessage("You do not teach Biology in 1a");
    }
}