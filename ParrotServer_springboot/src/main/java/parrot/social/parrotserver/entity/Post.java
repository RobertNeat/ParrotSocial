package parrot.social.parrotserver.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Post entity representing a user's post in the social platform")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Unique post identifier",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(
        description = "User who created the post",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(
        description = "Post content",
        example = "Just deployed my first Spring Boot application!",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @Column(name = "image_url", length = 500)
    @Schema(
        description = "URL of the post image (optional)",
        example = "https://example.com/images/post.jpg",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String imageUrl;

    @Column(name = "likes_count", nullable = false)
    @Schema(
        description = "Number of likes on the post",
        example = "42",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer likesCount = 0;

    @Column(name = "comments_count", nullable = false)
    @Schema(
        description = "Number of comments on the post",
        example = "15",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer commentsCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
        description = "Timestamp when the post was created",
        example = "2024-01-15T10:30:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(
        description = "Timestamp when the post was last updated",
        example = "2024-01-20T15:45:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Media attachments for this post",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Media> mediaAttachments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
        description = "Comments on this post",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        indexes = {
            @Index(name = "idx_post_tags_post", columnList = "post_id"),
            @Index(name = "idx_post_tags_tag", columnList = "tag_id")
        }
    )
    @Schema(
        description = "Tags associated with this post",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Tag> tags;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (likesCount == null) {
            likesCount = 0;
        }
        if (commentsCount == null) {
            commentsCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
