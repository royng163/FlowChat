package project.flowchat.backend.Model;

import java.time.ZonedDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
@Entity
@Table(name = "Message")
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "user_id_from")
    private Integer userIdFrom;

    @Column(name = "user_id_to")
    private Integer userIdTo;

    @Column(name = "content")
    private String content;

    @Column(name = "attach_to")
    private Integer attachTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    @Column(name = "read_at")
    private ZonedDateTime readAt;
}