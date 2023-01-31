package pl.schoolmanagementsystem.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record PasswordDto(@NotNull @NotEmpty String password,
                          @NotNull @NotEmpty String confirmPassword) {

}
