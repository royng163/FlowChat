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
@Table(name = "User_Profile")
public class UserProfileModel {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "description")
    private String description;

    @Column(name = "avatar")
    private Integer avatarId;

    @Column(name = "following_setting")
    private String followingSetting;

    @Column(name = "is_posting_visible")
    private Boolean isPostingVisible;

    @Column(name = "last_update")
    private ZonedDateTime UpdatedAt;
}