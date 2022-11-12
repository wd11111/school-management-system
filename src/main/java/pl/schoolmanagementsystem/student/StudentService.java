package pl.schoolmanagementsystem.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<StudentOutputDto3> getAllStudentsInClassWithMarksOfTheSubject(String schoolClassName, String subjectName) {
        return studentRepository.findAllInClassWithMarksOfTheSubject(schoolClassName, subjectName)
                .stream()
                .map(studentMapper::mapStudentToOutputDto3)
                .collect(Collectors.toList());
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String schoolClassName) {
        return studentRepository.findAllInClass(schoolClassName);
    }

    @Transactional
    public void deleteStudent(int studentId) {
        studentRepository.deleteById(studentId);
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchStudentException(id));
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail_Email(email)
                .orElseThrow(() -> new NoSuchStudentEmailException(email));
    }

    public int getIdFromPrincipals(Principal principal) {
        return studentRepository.findIdByEmail(principal.getName());
    }

    public void checkIfStudentExists(int studentId) {
        boolean doesStudentExist = studentRepository.existsById(studentId);
        if (!doesStudentExist) {
            throw new NoSuchStudentException(studentId);
        }
    }
}
