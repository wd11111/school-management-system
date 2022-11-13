package pl.schoolmanagementsystem.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherEmailException;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachSubjectException;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void addSubjectToTeacher(Teacher teacher, SchoolSubject schoolSubject) {
        teacher.getTaughtSubjects().add(schoolSubject);
    }

    @Transactional
    public void deleteById(int teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    public Teacher findByIdOrThrow(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    public List<SubjectAndClassOutputDto> findTaughtClassesByTeacher(int teacherId) {
        return teacherRepository.findTaughtClassesByTeacher(teacherId);
    }

    public List<Teacher> findAllTeachersInSchool() {
        return teacherRepository.findAll();
    }


    public Teacher findByEmailOrThrow(String email) {
        return teacherRepository.findByEmail_Email(email)
                .orElseThrow(() -> new NoSuchTeacherEmailException(email));
    }

    public void makeSureTeacherTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (!doesTeacherAlreadyTeachThisSubject(teacher, schoolSubject)) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    public void checkIfTeacherDoesntAlreadyTeachThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (doesTeacherAlreadyTeachThisSubject(teacher, schoolSubject)) {
            throw new TeacherAlreadyTeachesSubjectException(teacher, schoolSubject);
        }
    }

    public void makeSureTeacherExists(int teacherId) {
        boolean doesTeacherExist = teacherRepository.existsById(teacherId);
        if (!doesTeacherExist) {
            throw new NoSuchTeacherException(teacherId);
        }
    }

    public int getIdFromPrincipals(Principal principal) {
        return teacherRepository.findIdByEmail(principal.getName());
    }

    private boolean doesTeacherAlreadyTeachThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }
}
