package parrot.social.parrotserver.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Message threads organizing direct messages between multiple users or group conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique conversation identifier",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(length = 100)
    @Schema(
            description = "Conversation title (mainly for group conversations)",
            example = "Project Team Chat",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    private String title;

    @Column(name = "cover_image_url", length = 500)
    @Schema(
            description = "Cover image URL for the conversation",
            example = "https://example.com/covers/conversation123.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "conversation_type", nullable = false, length = 20)
    @Schema(
            description = "Type of conversation",
            example = "DIRECT",
            allowableValues = {"DIRECT", "GROUP"}
    )
    private ConversationType conversationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @Schema(
            description = "User who created the conversation",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User createdBy;

    @Column(name = "last_message_at")
    @Schema(
            description = "Timestamp of the last message in conversation",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime lastMessageAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "conversation_members",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"),
        indexes = {
            @Index(name = "idx_conversation_members_conversation", columnList = "conversation_id"),
            @Index(name = "idx_conversation_members_user", columnList = "user_id")
        }
    )
    @Schema(
        description = "Users who are members of this conversation",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<User> members;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Timestamp when the conversation was created",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(
            description = "Timestamp when the conversation was last updated",
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

    public enum ConversationType {
        DIRECT, GROUP
    }
}
