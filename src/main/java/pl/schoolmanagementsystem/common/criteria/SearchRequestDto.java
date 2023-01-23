package pl.schoolmanagementsystem.common.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {

    private String column;
    private String value;
    private Operation operation;
    private ValueType valueType;

    public enum Operation {
        EQUAL, LIKE, BETWEEN
    }

    public enum ValueType {
        STRING, NUMBER, DATE
    }
}
