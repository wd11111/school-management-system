package pl.schoolmanagementsystem.common.password;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordDto {

    private String password;
    private String encodedPassword;
}
