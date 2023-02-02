package pl.schoolmanagementsystem.student.search;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.schoolmanagementsystem.common.criteria.CriteriaApiFilterService;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.StudentRepository;

@Configuration
public class StudentSearcherConfig {

    @Bean
    @ConditionalOnProperty(name = "use.querydsl", havingValue = "true")
    public StudentSearcher studentSearcherQueryDSL(StudentRepository studentRepository) {
        return new StudentSearcherQueryDSL(studentRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "use.querydsl", havingValue = "false")
    public StudentSearcher studentSearcherCriteriaApi(StudentRepository studentRepository, CriteriaApiFilterService<Student> criteriaApiFilterService) {
        return new StudentSearcherCriteriaApi(studentRepository, criteriaApiFilterService);
    }

}
