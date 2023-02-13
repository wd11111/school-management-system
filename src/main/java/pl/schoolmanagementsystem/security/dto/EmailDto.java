package pl.schoolmanagementsystem.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record EmailDto(@NotNull(message = "{not.null.message}")
                       @NotEmpty (message = "{not.empty.message}")
                       String email) {
}
