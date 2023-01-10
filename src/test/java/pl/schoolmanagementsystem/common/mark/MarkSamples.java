package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

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
        return new MarkDto(4.0, SUBJECT);
    }

    default MarkDto createMarkDto2() {
        return new MarkDto(2.0, SUBJECT_2);
    }
}
