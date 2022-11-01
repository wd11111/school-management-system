package pl.schoolmanagementsystem.Mark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkWithList {

    private String subject;
    private Collection<Integer> marks;
}
