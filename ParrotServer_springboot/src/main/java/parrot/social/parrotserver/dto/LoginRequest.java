package parrot.social.parrotserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login credentials")
public class LoginRequest {

    @Schema(
        description = "Username for authentication",
        example = "john_doe",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(
        description = "User password",
        example = "SecurePassword123!",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "password"
    )
    @NotBlank(message = "Password is required")
    private String password;
}
