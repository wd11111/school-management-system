package pl.schoolmanagementsystem.common.mark.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MarkWithTwoFields {

    private byte mark;
    private String subject;

}
