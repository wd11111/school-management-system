package pl.schoolmanagementsystem.common.criteria;

import pl.schoolmanagementsystem.common.exception.FilterException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FilterUtil {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String SPLIT_REGEX = "to";

    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return LocalDate.parse(date, pattern);
        } catch (DateTimeParseException e) {
            throw new FilterException("Wrong date pattern! Use: " + DATE_PATTERN);
        }
    }

    public static Long parseNumber(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new FilterException("Value does not contain a parsable number!");
        }
    }

    public static String[] doSplit(String value) {
        validateRegexCorrectness(value);
        String[] splitted = value.split(SPLIT_REGEX);
        validateArrayLength(splitted);
        return splitted;
    }

    private static void validateRegexCorrectness(String value) {
        if (!value.contains(SPLIT_REGEX)) {
            throw new FilterException("Wrong split regex! Use: " + SPLIT_REGEX);
        }
    }

    private static void validateArrayLength(String[] splitted) {
        if (splitted.length != 2) {
            throw new FilterException("Wrong format!");
        }
    }

}
