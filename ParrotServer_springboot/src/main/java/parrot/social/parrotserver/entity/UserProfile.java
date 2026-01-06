package parrot.social.parrotserver.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name="userProfile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Extended user information and preferences beyond basic authorization data")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique user identifier",
            example ="1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(
            description = "Associated user account",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User user;

    @Column(name = "display_name", length = 100)
    @Schema(
            description = "User display name",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    private String displayName;

    @Column(name = "bio", length = 500)
    @Schema(
            description = "User biography or description",
            example = "Software developer passionate about open source",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    private String bio;

    @Column(name = "external_profile_url_1", length = 255)
    @Schema(
            description = "First external profile URL",
            example = "https://github.com/johndoe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    private String externalProfileUrl1;

    @Column(name = "external_profile_url_2", length = 255)
    @Schema(
            description = "Second external profile URL",
            example = "https://linkedin.com/in/johndoe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    private String externalProfileUrl2;

    @Column(name = "external_profile_url_3", length = 255)
    @Schema(
            description = "Third external profile URL",
            example = "https://twitter.com/johndoe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    private String externalProfileUrl3;

    @Column(name = "external_profile_url_4", length = 255)
    @Schema(
            description = "Fourth external profile URL",
            example = "https://instagram.com/johndoe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    private String externalProfileUrl4;

    @Column(name = "profile_picture_url", length = 500)
    @Schema(
            description = "Profile picture URL",
            example = "https://example.com/profile/johndoe.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    private String profilePictureUrl;

    @Column(name = "cover_photo_url", length = 500)
    @Schema(
            description = "Cover photo URL",
            example = "https://example.com/covers/johndoe.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    private String coverPhotoUrl;

    @Column(length = 20)
    @Schema(
            description = "User gender",
            example = "Male",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            allowableValues = {"Male", "Female", "Other", "Prefer not to say"}
    )
    private String gender;

    @Column(length = 100)
    @Schema(
            description = "User location",
            example = "New York, USA",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    private String location;

    @Column(name = "is_private", nullable = false)
    @Schema(
            description = "Whether the profile is private",
            example = "false",
            defaultValue = "false"
    )
    private Boolean isPrivate = false;

    @Column(name = "follower_count", nullable = false)
    @Schema(
            description = "Number of followers",
            example = "150",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer followerCount = 0;

    @Column(name = "following_count", nullable = false)
    @Schema(
            description = "Number of users being followed",
            example = "75",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer followingCount = 0;

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
