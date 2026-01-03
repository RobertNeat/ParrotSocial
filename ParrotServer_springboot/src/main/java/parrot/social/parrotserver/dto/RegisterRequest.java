package parrot.social.parrotserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration request data")
public class RegisterRequest {

    @Schema(
        description = "Unique username (3-50 characters)",
        example = "john_doe",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 50
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(
        description = "Valid email address",
        example = "john.doe@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "email"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(
        description = "Password (minimum 6 characters)",
        example = "SecurePassword123!",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 6,
        format = "password"
    )
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(
        description = "Display name for the user profile (optional, max 100 characters)",
        example = "John Doe",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;
}
