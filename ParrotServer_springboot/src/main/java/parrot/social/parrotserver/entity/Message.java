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
@Table(name="messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Messages sent between users in conversations")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique message identifier",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @Schema(
            description = "User who sent the message",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @Schema(
            description = "Conversation where the message was sent",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Conversation conversation;

    @Column(columnDefinition = "TEXT")
    @Schema(
            description = "Message content",
            example = "Hello, how are you?",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    @Schema(
            description = "Type of message",
            example = "TEXT",
            allowableValues = {"TEXT", "IMAGE", "VIDEO", "AUDIO", "FILE", "SYSTEM"}
    )
    private MessageType messageType = MessageType.TEXT;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Schema(
            description = "Message status",
            example = "SENT",
            allowableValues = {"SENT", "DELIVERED", "READ", "DELETED"}
    )
    private MessageStatus status = MessageStatus.SENT;

    @Column(name = "is_edited", nullable = false)
    @Schema(
            description = "Whether the message has been edited",
            example = "false",
            defaultValue = "false"
    )
    private Boolean isEdited = false;

    @Column(name = "edited_at")
    @Schema(
            description = "Timestamp when the message was edited",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime editedAt;

    @Column(name = "reply_to_message_id")
    @Schema(
            description = "ID of the message this is replying to",
            example = "123",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long replyToMessageId;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(
            description = "Media attachments for this message",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Media> mediaAttachments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "message_tags",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        indexes = {
            @Index(name = "idx_message_tags_message", columnList = "message_id"),
            @Index(name = "idx_message_tags_tag", columnList = "tag_id")
        }
    )
    @Schema(
        description = "Tags associated with this message",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Set<Tag> tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Timestamp when the message was created",
            example = "2023-12-01T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(
            description = "Timestamp when the message was last updated",
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

    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO, FILE, SYSTEM
    }

    public enum MessageStatus {
        SENT, DELIVERED, READ, DELETED
    }
}
