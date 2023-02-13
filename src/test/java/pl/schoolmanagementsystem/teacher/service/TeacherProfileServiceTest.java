package pl.schoolmanagementsystem.teacher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.*;
import pl.schoolmanagementsystem.teacher.dto.AddMarkDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.utils.MarkMapper;
import pl.schoolmanagementsystem.teacher.utils.MarkMapperStub;
import pl.schoolmanagementsystem.teacher.utils.StudentMapper;
import pl.schoolmanagementsystem.teacher.utils.StudentMapperStub;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static pl.schoolmanagementsystem.common.model.MarkEnum.E;

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

    @Spy
    private StudentMapper studentMapper = new StudentMapperStub();

    @Spy
    private MarkMapper markMapper =  new MarkMapperStub();

    @InjectMocks
    private TeacherProfileService teacherProfileService;

    @Test
    void should_correctly_return_taught_classes_by_teacher() {
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
        AddMarkDto addMarkDto = new AddMarkDto(E, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(student));
        when(subjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(schoolSubject));
        when(teacherInClassRepository
                .existsByTeacher_AppUser_EmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(true);

        teacherProfileService.addMark(NAME2, addMarkDto, student.getId());

        assertThat(student.getMarks()).hasSize(1);
    }

    @Test
    void should_throw_exception_when_trying_to_add_mark_to_student_but_one_doesnt_exist() {
        AddMarkDto addMarkDto = new AddMarkDto(E, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherProfileService.addMark(NAME2, addMarkDto, ID_1))
                .isInstanceOf(NoSuchStudentException.class)
                .hasMessage("Student with such an id does not exist: " + ID_1);
    }

    @Test
    void should_throw_exception_when_trying_to_add_mark_to_student_but_given_subject_is_not_found() {
        Student student = createStudent();
        AddMarkDto addMarkDto = new AddMarkDto(E, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(student));
        when(subjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherProfileService.addMark(NAME2, addMarkDto, ID_1))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + addMarkDto.getSubject());
        assertThat(student.getMarks()).hasSize(0);
    }

    @Test
    void should_throw_exception_when_teacher_is_trying_to_add_mark_in_class_it_doesnt_teach() {
        Student student = createStudent();
        SchoolSubject schoolSubject = createSchoolSubject();
        AddMarkDto addMarkDto = new AddMarkDto(E, SUBJECT_BIOLOGY);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(subjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherInClassRepository
                .existsByTeacher_AppUser_EmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.addMark(NAME2, addMarkDto, ID_1))
                .isInstanceOf(TeacherDoesNotTeachClassException.class)
                .hasMessage("You do not teach biology in 1a");
        assertThat(student.getMarks()).hasSize(0);
    }

    @Test
    void should_return_all_students_in_class_with_marks_of_given_subject() {
        SchoolClass schoolClass = createSchoolClass();
        schoolClass.getStudents().addAll(List.of(createStudent(), createStudent2()));
        when(subjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherInClassRepository
                .existsByTeacher_AppUser_EmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(true);
        when(classRepository.findClassAndFetchStudentsWithMarks(any())).thenReturn(Optional.of(schoolClass));

        List<StudentWithMarksDto> result = teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2);

        assertThat(result).hasSize(2);
    }

    @Test
    void should_correctly_filter_marks_when_returning_students_with_marks() {
        Student student = createStudent();
        student.addMark(new Mark(1L, getMarkAsBigDecimal2(), ID_1, SUBJECT_HISTORY));
        SchoolClass schoolClass = createSchoolClass();
        schoolClass.getStudents().add(student);
        when(subjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherInClassRepository
                .existsByTeacher_AppUser_EmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(true);
        when(classRepository.findClassAndFetchStudentsWithMarks(any())).thenReturn(Optional.of(schoolClass));

        List<StudentWithMarksDto> result = teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2);

        assertThat(result.get(0).getMarks()).hasSize(0);
    }

    @Test
    void should_throw_exception_when_trying_to_get_list_of_students_of_given_class_with_marks_of_given_subject_but_given_subject_doesnt_exist() {
        when(subjectRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + SUBJECT_BIOLOGY);
    }

    @Test
    void should_throw_exception_when_trying_to_get_list_of_students_of_given_class_with_marks_of_given_subject_but_given_class_doesnt_exist() {
        when(subjectRepository.existsById(anyString())).thenReturn(true);
        when(classRepository.findClassAndFetchStudentsWithMarks(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + CLASS_1A);
    }

    @Test
    void should_throw_exception_when_teacher_trying_to_get_students_of_class_it_doesnt_teach() {
        when(subjectRepository.existsById(anyString())).thenReturn(true);
        when(classRepository.findClassAndFetchStudentsWithMarks(any())).thenReturn(Optional.of(createSchoolClass()));
        when(teacherInClassRepository
                .existsByTeacher_AppUser_EmailAndTaughtSubjectAndTaughtClasses_Name(any(), any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> teacherProfileService.getClassStudentsWithMarksOfSubject(CLASS_1A, SUBJECT_BIOLOGY, NAME2))
                .isInstanceOf(TeacherDoesNotTeachClassException.class)
                .hasMessage("You do not teach biology in 1a");
    }
}