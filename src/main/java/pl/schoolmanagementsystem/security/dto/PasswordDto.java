package pl.schoolmanagementsystem.security.dto;

import pl.schoolmanagementsystem.common.annotation.FieldsValueMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
public record PasswordDto(@NotNull @NotEmpty String password,
                          @NotNull @NotEmpty String confirmPassword) {

}
