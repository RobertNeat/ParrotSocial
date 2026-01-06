package parrot.social.parrotserver.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Temporary content that disappears after 24 hours, similar to Instagram Stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique story identifier",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(
            description = "User who created the story",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User user;

    @Column(name = "content_url", length = 500, nullable = false)
    @Schema(
            description = "URL of the story content (image or video)",
            example = "https://example.com/stories/story123.jpg",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 500
    )
    private String contentUrl;

    @Column(name = "is_public", nullable = false)
    @Schema(
            description = "Whether the story is visible to all users or just followers",
            example = "true",
            defaultValue = "true"
    )
    private Boolean isPublic = true;

    @Column(name = "expires_at", nullable = false)
    @Schema(
            description = "Timestamp when the story expires (24 hours after creation)",
            example = "2023-12-02T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime expiresAt;

    @Column(name = "view_count", nullable = false)
    @Schema(
            description = "Number of times the story has been viewed",
            example = "25",
            defaultValue = "0",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer viewCount = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "story_viewers",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"),
        indexes = {
            @Index(name = "idx_story_viewers_story", columnList = "story_id"),
            @Index(name = "idx_story_viewers_user", columnList = "user_id")
        }
    )
    @Schema(
            description = "Users who have viewed this story",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<User> viewers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "story_tags",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        indexes = {
            @Index(name = "idx_story_tags_story", columnList = "story_id"),
            @Index(name = "idx_story_tags_tag", columnList = "tag_id")
        }
    )
    @Schema(
            description = "Tags associated with this story",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Tag> tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Timestamp when the story was created",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        expiresAt = LocalDateTime.now().plusHours(24); // Story expires after 24 hours
    }

    @Schema(
            description = "Check if the story has expired",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
