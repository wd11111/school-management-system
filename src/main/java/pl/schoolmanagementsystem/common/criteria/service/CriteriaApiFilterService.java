package pl.schoolmanagementsystem.common.criteria.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.criteria.dto.SearchRequestDto;
import pl.schoolmanagementsystem.common.exception.FilterException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static pl.schoolmanagementsystem.common.criteria.util.FilterUtil.*;

@Service
public class CriteriaApiFilterService<T> {

    public Specification<T> getSearchSpecification(List<SearchRequestDto> searchRequestDtos) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(searchRequestDtos.size());

            searchRequestDtos.forEach(searchRequestDto -> {
                String column = searchRequestDto.column();
                String value = searchRequestDto.value();

                switch (searchRequestDto.operation()) {
                    case EQUAL -> doEqualOperation(column, value, predicates, root, criteriaBuilder);
                    case LIKE -> doLikeOperation(column, value, predicates, root, criteriaBuilder);
                    case NUMBER_BETWEEN -> doNumberBetweenOperation(column, value, predicates, root, criteriaBuilder);
                    case DATE_BETWEEN -> doDateBetweenOperation(column, value, predicates, root, criteriaBuilder);
                    default -> throw new FilterException("Unexpected operation type");
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void doEqualOperation(String column, String value, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate equal = criteriaBuilder.equal(root.get(column), value);
        predicates.add(equal);
    }

    private void doLikeOperation(String column, String value, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate like = criteriaBuilder.like(root.get(column), "%" + value + "%");
        predicates.add(like);
    }

    private void doNumberBetweenOperation(String column, String value, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        String[] splitted = doSplit(value);

        Long from = parseNumber(splitted[0]);
        Long to = parseNumber(splitted[1]);

        Predicate betweenNumber = criteriaBuilder.between(root.get(column), from, to);
        predicates.add(betweenNumber);
    }

    private void doDateBetweenOperation(String column, String value, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        String[] splitted = doSplit(value);

        LocalDate from = getLocalDate(splitted[0]);
        LocalDate to = getLocalDate(splitted[1]);

        Predicate betweenDate = criteriaBuilder.between(root.get(column), from, to);
        predicates.add(betweenDate);
    }

}
