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

@Service
public class FilterService<T> {

    public static final String UNEXPECTED_VALUE_TYPE_MESSAGE = "Unexpected value type";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String SPLIT_REGEX = "to";

    public Specification<T> getSearchSpecification(List<SearchRequestDto> searchRequestDtos) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(searchRequestDtos.size());

            searchRequestDtos.forEach(searchRequestDto -> {
                switch (searchRequestDto.getOperation()) {
                    case EQUAL -> doEqualOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case LIKE -> doLikeOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case BETWEEN_NUMBER -> doBetweenNumberOperation(searchRequestDto, predicates, root, criteriaBuilder);
                    case BETWEEN_DATE -> doBetweenDateOperation(root, criteriaBuilder, predicates, searchRequestDto);
                    default -> throw new IllegalArgumentException("Unexpected operation type");
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void doEqualOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate equal = criteriaBuilder.equal(root.get(searchRequestDto.getColumn()), searchRequestDto.getValue());
        predicates.add(equal);
    }

    private void doLikeOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate like = criteriaBuilder.like(root.get(searchRequestDto.getColumn()), "%" + searchRequestDto.getValue() + "%");
        predicates.add(like);

    }

    private void doBetweenNumberOperation(SearchRequestDto searchRequestDto, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder) {
        String[] splitted = doSplit(searchRequestDto.getValue());
        Predicate betweenNumber = criteriaBuilder.between(root.get(searchRequestDto.getColumn()), Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
        predicates.add(betweenNumber);
    }

    private void doBetweenDateOperation(Root<T> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, SearchRequestDto searchRequestDto) {
        String[] splitted = doSplit(searchRequestDto.getValue());
        Predicate betweenDate = criteriaBuilder.between(root.get(searchRequestDto.getColumn()), getLocalDate(splitted[0]), getLocalDate(splitted[1]));
        predicates.add(betweenDate);
    }

    private LocalDate getLocalDate(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return LocalDate.parse(date, pattern);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Wrong date pattern! Use: " + DATE_PATTERN);
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
            throw new IllegalArgumentException("Wrong split regex! Use: " + SPLIT_REGEX);
        }
    }

    private void validateArrayLength(String[] splitted) {
        if (splitted.length < 2) {
            throw new IllegalArgumentException();
        }
    }
}
