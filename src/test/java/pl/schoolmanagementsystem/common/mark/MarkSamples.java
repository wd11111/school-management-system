package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.MarkDto;

public interface MarkSamples {

    String SUBJECT = "Biology";
    String SUBJECT_2 = "History";

    default Mark createMark() {
        return Mark.builder()
                .id(1)
                .mark((byte) 4)
                .studentId(3)
                .build();
    }

    default Mark createMark2() {
        return Mark.builder()
                .id(2)
                .mark((byte) 2)
                .studentId(3)
                .build();
    }

    default MarkDto createMarkDto1() {
        return new MarkDto((byte) 4, SUBJECT);
    }

    default MarkDto createMarkDto2() {
        return new MarkDto((byte) 2, SUBJECT_2);
    }
}
