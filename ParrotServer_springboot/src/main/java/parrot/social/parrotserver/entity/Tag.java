package parrot.social.parrotserver.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Content categorization tags for discovery and trending topics")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique tag identifier",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    @Schema(
            description = "Tag name",
            example = "technology",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    private String name;

    @Column(name = "usage_count", nullable = false)
    @Schema(
            description = "Number of times this tag has been used",
            example = "150",
            defaultValue = "0",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer usageCount = 0;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Schema(
            description = "Posts that use this tag",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Post> posts;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Schema(
            description = "Stories that use this tag",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Story> stories;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Schema(
            description = "Messages that use this tag",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Message> messages;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Timestamp when the tag was created",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(
            description = "Timestamp when the tag was last updated",
            example = "2023-12-01T10:30:00",
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
