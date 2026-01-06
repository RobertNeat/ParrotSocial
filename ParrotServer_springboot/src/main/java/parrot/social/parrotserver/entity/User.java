package parrot.social.parrotserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User entity representing a registered user in the system")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Unique user identifier",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(
        description = "Unique username (3-50 characters)",
        example = "john_doe",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 50
    )
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(
        description = "User email address",
        example = "john.doe@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "email"
    )
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(
        description = "Encrypted password (write-only)",
        example = "john_doe",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "password",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @Column(length = 50)
    @Schema(
        description = "User roles (comma-separated)",
        example = "ROLE_USER",
        defaultValue = "ROLE_USER",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String roles = "ROLE_USER";

    @Column(name = "is_verified", nullable = false)
    @Schema(
        description = "Whether the user's email address has been verified",
        example = "false",
        defaultValue = "false"
    )
    private Boolean isVerified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
        description = "Timestamp when the user was created",
        example = "2024-01-15T10:30:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(
        description = "Timestamp when the user was last updated",
        example = "2024-01-20T15:45:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Posts created by this user",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Comments created by this user",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Comment> comments;

    @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Media uploaded by this user",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Media> uploadedMedia;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Stories created by this user",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Story> stories;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Messages sent by this user",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Message> sentMessages;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "User profile information",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private UserProfile profile;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
