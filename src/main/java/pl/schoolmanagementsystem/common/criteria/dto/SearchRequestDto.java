package pl.schoolmanagementsystem.common.criteria.dto;

public record SearchRequestDto(String column,
                               String value,
                               OperationType operation) {
}
