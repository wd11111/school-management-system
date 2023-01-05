package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.service.AdminClassService;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.TeacherInClassRequestDto;
import pl.schoolmanagementsystem.model.dto.TeacherInClassResponseDto;
import pl.schoolmanagementsystem.model.dto.SubjectAndTeacherResponseDto;
import pl.schoolmanagementsystem.model.dto.StudentResponseDto2;

import javax.validation.Valid;
import java.util.List;

import static pl.schoolmanagementsystem.mapper.TeacherInClassMapper.mapToTeacherInClassResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/classes")
public class AdminClassController {

    private final AdminClassService adminClassService;

    @GetMapping
    public Page<SchoolClassDto> getSchoolClasses(Pageable pageable) {
        return adminClassService.getSchoolClasses(pageable);
    }

    @GetMapping("/{className}")// todo tu bym zmienił może na "/{className}/student" adekwatnie do tego co masz niżej.
    public List<StudentResponseDto2> getAllStudentsInClass(@PathVariable String className) {
        return adminClassService.getAllStudentsInClass(className);
    }

    @GetMapping("/{className}/subjects")
    public List<SubjectAndTeacherResponseDto> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return adminClassService.getTaughtSubjectsInClass(className);
    }

    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody @Valid SchoolClassDto schoolClassDto) {
        adminClassService.createSchoolClass(schoolClassDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolClassDto);
    }

    @PatchMapping("/{className}/teachers")
    public TeacherInClassResponseDto addTeacherToSchoolClass(
            @PathVariable String className, @RequestBody @Valid TeacherInClassRequestDto teacherInClassRequestDto) {
        return mapToTeacherInClassResponseDto(adminClassService.addTeacherToSchoolClass(teacherInClassRequestDto, className));
        // todo teoretycznie takie rzeczy powinny robić service.
        //  Wiem, że to nie istotne, ale nie którzy programiści się czepiają,
        // że controller nie powinien zajmować się niczym prócz wywoływania metod servicu lub tworzenia responseEntity.
        // powinno być coś takiego: adminClassService.mapToTeacherInClassResponseDto(xyz), a po tamtej stronie dokładnie to samo co tu
    }

    @DeleteMapping("/{className}")
    public ResponseEntity<Void> deleteSchoolClass(@PathVariable String className) {
        adminClassService.deleteSchoolClass(className);
        return ResponseEntity.noContent().build();
    }
}
