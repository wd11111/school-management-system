package pl.schoolmanagementsystem.student.search;

import pl.schoolmanagementsystem.common.criteria.dto.SearchRequestDto;
import pl.schoolmanagementsystem.common.model.Student;

import java.util.List;

public interface StudentSearcher {

    List<Student> searchStudent(List<SearchRequestDto> searchRequestDtos);
}
