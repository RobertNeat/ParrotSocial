package parrot.social.parrotserver.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Digital assets (images,videos,audio) attached to posts, stories, or messages")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique media identifier",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @Schema(
            description = "Associated post (if media is attached to a post)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    @Schema(
            description = "Associated story (if media is attached to a story)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @Schema(
            description = "Associated message (if media is attached to a message)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Message message;

    @Column(name = "file_url", length = 500, nullable = false)
    @Schema(
            description = "URL of the media file",
            example = "https://example.com/media/image123.jpg",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 500
    )
    private String fileUrl;

    @Column(name = "thumbnail_url", length = 500)
    @Schema(
            description = "URL of the thumbnail/preview image",
            example = "https://example.com/thumbnails/image123_thumb.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    private String thumbnailUrl;

    @Column(name = "file_size")
    @Schema(
            description = "File size in bytes",
            example = "1048576",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long fileSize;

    @Column(precision = 10, scale = 2)
    @Schema(
            description = "Duration in seconds (for video/audio files)",
            example = "120.50",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private BigDecimal duration;

    @Column(name = "width")
    @Schema(
            description = "Media width in pixels",
            example = "1920",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer width;

    @Column(name = "height")
    @Schema(
            description = "Media height in pixels",
            example = "1080",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer height;

    @Column(name = "alt_text", length = 500)
    @Schema(
            description = "Alternative text for accessibility",
            example = "A beautiful sunset over the ocean",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    private String altText;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Timestamp when the media was created",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(
            description = "Timestamp when the media was last updated",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    @Schema(
        description = "User who uploaded this media",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User uploadedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 20)
    @Schema(
        description = "Type of media",
        example = "IMAGE",
        allowableValues = {"IMAGE", "VIDEO", "AUDIO", "DOCUMENT"}
    )
    private MediaType mediaType;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MediaType {
        IMAGE, VIDEO, AUDIO, DOCUMENT
    }
}
