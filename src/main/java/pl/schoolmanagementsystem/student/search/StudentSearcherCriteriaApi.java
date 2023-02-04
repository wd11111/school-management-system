package pl.schoolmanagementsystem.student.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import pl.schoolmanagementsystem.common.criteria.dto.SearchRequestDto;
import pl.schoolmanagementsystem.common.criteria.service.CriteriaApiFilterService;
import pl.schoolmanagementsystem.common.exception.FilterException;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.StudentRepository;

import java.util.List;

@RequiredArgsConstructor
public class StudentSearcherCriteriaApi implements StudentSearcher {

    private final StudentRepository studentRepository;

    private final CriteriaApiFilterService<Student> criteriaApiFilterService;

    @Override
    public List<Student> searchStudent(List<SearchRequestDto> searchRequestDtos) {
        Specification<Student> searchSpecification = criteriaApiFilterService.getSearchSpecification(searchRequestDtos);
        try {
            return studentRepository.findAll(searchSpecification);
        } catch (RuntimeException e) {
            throw new FilterException(e.getMessage().split(";")[0]);
        }
    }
}
