package project.flowchat.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor

@Data
@Entity
@Table(name = "Authentication")
public class LicenseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_id")
    private Integer keyId;

    @Column(name = "key_code")
    private String keyCode;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "is_available")
    private Boolean isAvailable;
}