package pl.schoolmanagementsystem.Model.dto;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkWithList {

    private String subject;
    private Collection<Integer> marks;
}
