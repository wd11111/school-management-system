package pl.schoolmanagementsystem.common.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String column;
    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String value;
    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private Operation operation;
    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private ValueType valueType;

    public enum Operation {
        EQUAL, LIKE, BETWEEN
    }

    public enum ValueType {
        STRING, NUMBER, DATE
    }
}
