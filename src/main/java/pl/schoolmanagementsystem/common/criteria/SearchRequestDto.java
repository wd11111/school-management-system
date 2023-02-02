package pl.schoolmanagementsystem.common.criteria;

public record SearchRequestDto(String column,
                               String value,
                               OperationType operation) {
}
