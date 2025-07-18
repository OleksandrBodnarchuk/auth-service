package pl.alex.auth.user.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank String password,
        @NotBlank
        @Email
        String email) {
}
