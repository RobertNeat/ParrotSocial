package parrot.social.parrotserver.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Comment entity representing a user's comment on a post")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Unique comment identifier",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @Schema(
        description = "Post that this comment belongs to",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(
        description = "User who created the comment",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(
        description = "Comment content",
        example = "Great post! Thanks for sharing.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @Schema(
        description = "Parent comment (for nested replies)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Comment parentComment;

    @Column(name = "likes_count", nullable = false)
    @Schema(
        description = "Number of likes on the comment",
        example = "5",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer likesCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
        description = "Timestamp when the comment was created",
        example = "2024-01-15T10:30:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(
        description = "Timestamp when the comment was last updated",
        example = "2024-01-20T15:45:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (likesCount == null) {
            likesCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
