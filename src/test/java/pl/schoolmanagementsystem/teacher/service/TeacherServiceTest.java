package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.teacher.TeacherRepository;
import pl.schoolmanagementsystem.teacher.TeacherService;

import static org.mockito.Mockito.mock;

class TeacherServiceTest implements TeacherSamples {

    private TeacherRepository teacherRepository = mock(TeacherRepository.class);

    private TeacherService teacherService = new TeacherService(teacherRepository);


}
