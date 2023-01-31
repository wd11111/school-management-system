package pl.schoolmanagementsystem.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record LoginCredentials(@NotNull @NotEmpty String email,
                               @NotNull @NotEmpty String password) {
}
