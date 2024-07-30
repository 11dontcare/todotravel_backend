package org.example.todotravel.domain.plan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @Id
    @Column(name = "bookmark_id", nullable = false)
    private String bookmarkId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User likeUser;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan planId;
}
