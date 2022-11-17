package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields;

public interface MarkSamples {

    String SUBJECT = "Biology";
    String SUBJECT_2 = "History";

    default Mark createMark() {
        return Mark.builder()
                .id(1)
                .mark(4)
                .studentId(3)
                .build();
    }

    default Mark createMark2() {
        return Mark.builder()
                .id(1)
                .mark(2)
                .studentId(3)
                .build();
    }

    default MarkWithTwoFields createMarkWithTwoFields() {
        return new MarkWithTwoFields(4, SUBJECT);
    }

    default MarkWithTwoFields createMarkWithTwoFields2() {
        return new MarkWithTwoFields(2, SUBJECT_2);
    }
}
