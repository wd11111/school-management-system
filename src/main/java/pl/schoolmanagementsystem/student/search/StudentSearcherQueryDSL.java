package pl.schoolmanagementsystem.student.search;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import pl.schoolmanagementsystem.common.criteria.SearchRequestDto;
import pl.schoolmanagementsystem.common.exception.FilterException;
import pl.schoolmanagementsystem.common.model.QStudent;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

import static pl.schoolmanagementsystem.common.criteria.FilterUtil.*;

@RequiredArgsConstructor
public class StudentSearcherQueryDSL implements StudentSearcher {

    private static final String INVALID_DATA_MESSAGE = "Invalid data";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String BIRTH_DATE = "birthDate";
    private static final String SCHOOL_CLASS = "schoolClass";

    private final static QStudent student = QStudent.student;

    private final StudentRepository studentRepository;

    @Override
    public List<Student> searchStudent(List<SearchRequestDto> searchRequestDtos) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        searchRequestDtos.forEach(searchRequestDto -> {
            String value = searchRequestDto.value();

            switch (searchRequestDto.operation()) {
                case EQUAL -> doEqualOperation(searchRequestDto, booleanBuilder, value);
                case LIKE -> doLikeOperation(searchRequestDto, booleanBuilder, value);
                case NUMBER_BETWEEN -> doNumberBetweenOperation(searchRequestDto, booleanBuilder, value);
                case DATE_BETWEEN -> doDateBetweenOperation(searchRequestDto, booleanBuilder, value);
                default -> throw new FilterException("Unexpected operation type");
            }
        });
        return (List<Student>) studentRepository.findAll(booleanBuilder);
    }

    private void doEqualOperation(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder, String value) {
        switch (searchRequestDto.column()) {
            case NAME -> booleanBuilder.and(student.name.equalsIgnoreCase(value));
            case SURNAME -> booleanBuilder.and(student.surname.equalsIgnoreCase(value));
            case EMAIL -> booleanBuilder.and(student.appUser.userEmail.equalsIgnoreCase(value));
            case SCHOOL_CLASS -> booleanBuilder.and(student.schoolClass.equalsIgnoreCase(value));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

    private void doLikeOperation(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder, String value) {
        switch (searchRequestDto.column()) {
            case NAME -> booleanBuilder.and(student.name.containsIgnoreCase(value));
            case SURNAME -> booleanBuilder.and(student.surname.containsIgnoreCase(value));
            case EMAIL -> booleanBuilder.and(student.appUser.userEmail.containsIgnoreCase(value));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

    private void doNumberBetweenOperation(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder, String value) {
        String[] splitted = doSplit(value);

        Long from = parseNumber(splitted[0]);
        Long to = parseNumber(splitted[1]);

        switch (searchRequestDto.column()) {
            case ID -> booleanBuilder.and(student.id.between(from, to));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

    private void doDateBetweenOperation(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder, String value) {
        String[] splitted = doSplit(value);

        LocalDate from = getLocalDate(splitted[0]);
        LocalDate to = getLocalDate(splitted[1]);

        switch (searchRequestDto.column()) {
            case BIRTH_DATE -> booleanBuilder.and(student.birthDate.between(from, to));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

}
