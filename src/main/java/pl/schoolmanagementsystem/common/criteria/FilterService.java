package pl.schoolmanagementsystem.common.criteria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

@Service
public class FilterService<T> {

    public static final String UNEXPECTED_VALUE_TYPE_MESSAGE = "Unexpected value type";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_SPLIT_REGEX = "to";

    public Specification<T> getSearchSpecification(List<SearchRequestDto> searchRequestDtos) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(searchRequestDtos.size());

            searchRequestDtos.forEach(searchRequestDto -> {
                switch (searchRequestDto.getOperation()) {
                    case EQUAL -> doEqualOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case LIKE -> doLikeOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case BETWEEN -> doBetweenOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    default -> throw new IllegalArgumentException("Unexpected operation type");
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void doEqualOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        switch (searchRequestDto.getValueType()) {
            case STRING -> {
                Predicate equal = criteriaBuilder.equal(root.get(searchRequestDto.getColumn()), searchRequestDto.getValue());
                predicates.add(equal);
            }
            case NUMBER -> {
                Predicate equal = criteriaBuilder.equal(root.get(searchRequestDto.getColumn()), Long.parseLong(searchRequestDto.getValue()));
                predicates.add(equal);
            }
            case DATE -> {
                LocalDate localDate = getLocalDate(searchRequestDto.getValue());
                Predicate equal = criteriaBuilder.equal(root.get(searchRequestDto.getColumn()), localDate);
                predicates.add(equal);
            }
            default -> throw new IllegalArgumentException(UNEXPECTED_VALUE_TYPE_MESSAGE);
        }
    }

    private void doLikeOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {

        switch (searchRequestDto.getValueType()) {
            case STRING -> {
                Predicate like = criteriaBuilder.like(root.get(searchRequestDto.getColumn()), "%" + searchRequestDto.getValue() + "%");
                predicates.add(like);
            }
            default -> throw new IllegalArgumentException(UNEXPECTED_VALUE_TYPE_MESSAGE);
        }

    }

    private void doBetweenOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        switch (searchRequestDto.getValueType()) {
            case NUMBER -> {
                String[] splitted = doSplit(searchRequestDto.getValue());
                Predicate between = criteriaBuilder.between(root.get(searchRequestDto.getColumn()), Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
                predicates.add(between);
            }
            case DATE -> {
                String[] splitted = doSplit(searchRequestDto.getValue());
                Predicate between = criteriaBuilder.between(root.get(searchRequestDto.getColumn()), getLocalDate(splitted[0]), getLocalDate(splitted[1]));
                predicates.add(between);
            }
            default -> throw new IllegalArgumentException(UNEXPECTED_VALUE_TYPE_MESSAGE);
        }
    }

    private LocalDate getLocalDate(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return LocalDate.parse(date, pattern);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("DateTimeParseException");
        }
    }

    private String[] doSplit(String value) {
        try {
            return value.split(DATE_SPLIT_REGEX);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("PatternSyntaxException");
        }
    }
}
