package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.common.student.Student;

import java.util.ArrayList;

public interface StudentSamples {

    String HUBERT = "Hubert";
    String CLASS_A = "1a";
    int STUDENT_ID = 1;
    int STUDENT_ID2 = 2;


    default Student createStudent() {
        return Student.builder()
                .id(1)
                .name(HUBERT)
                .schoolClass(CLASS_A)
                .marks(new ArrayList<>())
                .build();
    }

    default Student createStudent2() {
        return Student.builder()
                .id(STUDENT_ID2)
                .name(HUBERT)
                .schoolClass(CLASS_A)
                .marks(new ArrayList<>())
                .build();
    }
}
