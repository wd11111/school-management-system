package pl.schoolmanagementsystem.email;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @Id
    @Column(nullable = false, unique = true)
    private String email;
}
