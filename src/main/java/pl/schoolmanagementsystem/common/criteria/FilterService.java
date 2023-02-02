package pl.schoolmanagementsystem.common.criteria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.exception.FilterException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilterService<T> {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String SPLIT_REGEX = "to";

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
        Predicate betweenNumber = criteriaBuilder.between(root.get(searchRequestDto.column()), parseNumber(splitted[0]), parseNumber(splitted[1]));
        predicates.add(betweenNumber);
    }

    private void doDateBetweenOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        String[] splitted = doSplit(searchRequestDto.value());
        Predicate betweenDate = criteriaBuilder.between(root.get(searchRequestDto.column()), getLocalDate(splitted[0]), getLocalDate(splitted[1]));
        predicates.add(betweenDate);
    }

    private LocalDate getLocalDate(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return LocalDate.parse(date, pattern);
        } catch (DateTimeParseException e) {
            throw new FilterException("Wrong date pattern! Use: " + DATE_PATTERN);
        }
    }

    private String[] doSplit(String value) {
        validateRegexCorrectness(value);
        String[] splitted = value.split(SPLIT_REGEX);
        validateArrayLength(splitted);
        return splitted;
    }

    private void validateRegexCorrectness(String value) {
        if (!value.contains(SPLIT_REGEX)) {
            throw new FilterException("Wrong split regex! Use: " + SPLIT_REGEX);
        }
    }

    private void validateArrayLength(String[] splitted) {
        if (splitted.length != 2) {
            throw new FilterException("Wrong format!");
        }
    }

    private Long parseNumber(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new FilterException("Value does not contain a parsable number!");
        }
    }

}
