package pl.schoolmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private byte mark;//todo dlaczego byte? przy ocenie? w diagrami jest zaznaczone jako smallint.
    // Co z ocenami jak 3+, 3-? Przy takich okazjach często się wykorzystuje słowniki(można je zrobić za pomocą enuma),
    // dzięki czemu mamy określoną ilość ocen od np. 1 do 6 uwzględniając od 2- do 6-.

    private long studentId;

    private String subject;

    String s1 ="abc";
    String s2 ="abc";
    String s3 ="abc";
}
