package parrot.social.parrotserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response containing JWT token and user information")
public class LoginResponse {

    @Schema(
        description = "JWT authentication token (valid for 24 hours)",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String token;

    @Schema(
        description = "Authenticated username",
        example = "john_doe",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Schema(
            description = "Valid email address",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "email"
    )
    private String email;

    @Schema(
        description = "User roles (comma-separated)",
        example = "ROLE_USER",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String roles;
}
