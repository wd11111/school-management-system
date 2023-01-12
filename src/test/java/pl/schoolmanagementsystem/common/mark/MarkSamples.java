package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.math.BigDecimal;

public interface MarkSamples {

    String SUBJECT = "Biology";
    String SUBJECT_2 = "History";

    default Mark createMark() {
        return Mark.builder()
                .id(1)
                .mark(BigDecimal.valueOf(4.0))
                .studentId(3)
                .build();
    }

    default Mark createMark2() {
        return Mark.builder()
                .id(2)
                .mark(BigDecimal.valueOf(2.0))
                .studentId(3)
                .build();
    }

    default MarkDto createMarkDto1() {
        return new MarkDto(BigDecimal.valueOf(4.0), SUBJECT);
    }

    default MarkDto createMarkDto2() {
        return new MarkDto(BigDecimal.valueOf(2.0), SUBJECT_2);
    }

    default BigDecimal getMark1() {
        return BigDecimal.valueOf(4.0);
    }

    default BigDecimal getMark2() {
        return BigDecimal.valueOf(2.0);
    }
}
