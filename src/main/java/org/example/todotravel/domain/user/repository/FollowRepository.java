package org.example.todotravel.domain.user.repository;

import org.example.todotravel.domain.user.entity.Follow;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerUserAndFollowingUser(User followerUser, User followingUser);
    Optional<Follow> findByFollowerUserAndFollowingUser(User followerUser, User followingUser);

    @Query("SELECT f.followingUser FROM Follow f WHERE f.followerUser.userId = :userId")
    List<User> findFollowingsByUserId(@Param("userId") Long userId);

    @Query("SELECT f.followerUser FROM Follow f WHERE f.followingUser.userId = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);
}
