package pl.schoolmanagementsystem.common.criteria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.exception.FilterException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static pl.schoolmanagementsystem.common.criteria.FilterUtil.*;

@Service
public class CriteriaApiFilterService<T> {

    public Specification<T> getSearchSpecification(List<SearchRequestDto> searchRequestDtos) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(searchRequestDtos.size());

            searchRequestDtos.forEach(searchRequestDto -> {
                switch (searchRequestDto.operation()) {
                    case EQUAL -> doEqualOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case LIKE -> doLikeOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case NUMBER_BETWEEN -> doNumberBetweenOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case DATE_BETWEEN -> doDateBetweenOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    default -> throw new FilterException("Unexpected operation type");
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void doEqualOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate equal = criteriaBuilder.equal(root.get(searchRequestDto.column()), searchRequestDto.value());
        predicates.add(equal);
    }

    private void doLikeOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate like = criteriaBuilder.like(root.get(searchRequestDto.column()), "%" + searchRequestDto.value() + "%");
        predicates.add(like);
    }

    private void doNumberBetweenOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        String[] splitted = doSplit(searchRequestDto.value());

        Long from = parseNumber(splitted[0]);
        Long to = parseNumber(splitted[1]);

        Predicate betweenNumber = criteriaBuilder.between(root.get(searchRequestDto.column()), from, to);
        predicates.add(betweenNumber);
    }

    private void doDateBetweenOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        String[] splitted = doSplit(searchRequestDto.value());

        LocalDate from = getLocalDate(splitted[0]);
        LocalDate to = getLocalDate(splitted[1]);

        Predicate betweenDate = criteriaBuilder.between(root.get(searchRequestDto.column()), from, to);
        predicates.add(betweenDate);
    }

}
