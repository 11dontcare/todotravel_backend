package org.example.todotravel.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long refreshTokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "value", length = 500)
    private String value;
}
