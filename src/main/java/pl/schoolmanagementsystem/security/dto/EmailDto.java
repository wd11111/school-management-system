package pl.schoolmanagementsystem.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record EmailDto(@NotNull @NotEmpty String email) {
}
