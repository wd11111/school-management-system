package pl.schoolmanagementsystem.student.search;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import pl.schoolmanagementsystem.common.criteria.dto.SearchRequestDto;
import pl.schoolmanagementsystem.common.exception.FilterException;
import pl.schoolmanagementsystem.common.model.QStudent;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

import static pl.schoolmanagementsystem.common.criteria.util.FilterUtil.doSplit;
import static pl.schoolmanagementsystem.common.criteria.util.FilterUtil.parseNumber;
import static pl.schoolmanagementsystem.common.criteria.util.FilterUtil.parseToLocalDate;
import static pl.schoolmanagementsystem.student.search.StudentFieldsUtil.BIRTH_DATE;
import static pl.schoolmanagementsystem.student.search.StudentFieldsUtil.EMAIL;
import static pl.schoolmanagementsystem.student.search.StudentFieldsUtil.ID;
import static pl.schoolmanagementsystem.student.search.StudentFieldsUtil.NAME;
import static pl.schoolmanagementsystem.student.search.StudentFieldsUtil.SCHOOL_CLASS;
import static pl.schoolmanagementsystem.student.search.StudentFieldsUtil.SURNAME;

@RequiredArgsConstructor
public class StudentSearcherQueryDSL implements StudentSearcher {

    private static final String INVALID_DATA_MESSAGE = "Invalid data";

    private final static QStudent student = QStudent.student;

    private final StudentRepository studentRepository;

    @Override
    public List<Student> searchStudent(List<SearchRequestDto> searchRequestDtos) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        searchRequestDtos.forEach(searchRequestDto -> {
            String value = searchRequestDto.value();
            String column = searchRequestDto.column();

            switch (searchRequestDto.operation()) {
                case EQUAL -> doEqualOperation(column, value, booleanBuilder);
                case LIKE -> doLikeOperation(column, value, booleanBuilder);
                case NUMBER_BETWEEN -> doNumberBetweenOperation(column, value, booleanBuilder);
                case DATE_BETWEEN -> doDateBetweenOperation(column, value, booleanBuilder);
                default -> throw new FilterException("Unexpected operation type");
            }
        });
        return (List<Student>) studentRepository.findAll(booleanBuilder);
    }

    private void doEqualOperation(String column, String value, BooleanBuilder booleanBuilder) {
        switch (column) {
            case NAME -> booleanBuilder.and(student.name.equalsIgnoreCase(value));
            case SURNAME -> booleanBuilder.and(student.surname.equalsIgnoreCase(value));
            case EMAIL -> booleanBuilder.and(student.appUser.email.equalsIgnoreCase(value));
            case SCHOOL_CLASS -> booleanBuilder.and(student.schoolClass.equalsIgnoreCase(value));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

    private void doLikeOperation(String column, String value, BooleanBuilder booleanBuilder) {
        switch (column) {
            case NAME -> booleanBuilder.and(student.name.containsIgnoreCase(value));
            case SURNAME -> booleanBuilder.and(student.surname.containsIgnoreCase(value));
            case EMAIL -> booleanBuilder.and(student.appUser.email.containsIgnoreCase(value));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

    private void doNumberBetweenOperation(String column, String value, BooleanBuilder booleanBuilder) {
        String[] splitted = doSplit(value);

        Long from = parseNumber(splitted[0]);
        Long to = parseNumber(splitted[1]);

        switch (column) {
            case ID -> booleanBuilder.and(student.id.between(from, to));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

    private void doDateBetweenOperation(String column, String value, BooleanBuilder booleanBuilder) {
        String[] splitted = doSplit(value);

        LocalDate from = parseToLocalDate(splitted[0]);
        LocalDate to = parseToLocalDate(splitted[1]);

        switch (column) {
            case BIRTH_DATE -> booleanBuilder.and(student.birthDate.between(from, to));
            default -> throw new FilterException(INVALID_DATA_MESSAGE);
        }
    }

}
