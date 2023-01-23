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

    public enum Operation {
        EQUAL, LIKE, NUMBER_BETWEEN, DATE_BETWEEN
    }
}
