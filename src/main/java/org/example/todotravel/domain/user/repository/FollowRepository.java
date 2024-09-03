package org.example.todotravel.domain.user.repository;

import org.example.todotravel.domain.user.entity.Follow;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 두 사용자의 이메일로 팔로잉 중인지 확인
    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM Follow f
        WHERE f.followerUser.email = :followerEmail
        AND f.followingUser.email = :followingEmail
        """)
    boolean existsByFollowerEmailAndFollowingEmail(@Param("followerEmail") String followerEmail,
                                                   @Param("followingEmail") String followingEmail);

    // 이미 팔로우 중인지 검사
    boolean existsByFollowerUserAndFollowingUser(User followerUser, User followingUser);

    // 취소할 팔로우가 있는지 조회
    Optional<Follow> findByFollowerUserAndFollowingUser(User followerUser, User followingUser);

    // 특정 사용자를 팔로우하는 사람들 조회
    @Query("SELECT DISTINCT f.followerUser FROM Follow f WHERE f.followingUser.userId = :userId")
    Page<User> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    // 특정 사용자가 팔로잉하는 사람들 조회
    @Query("SELECT DISTINCT f.followingUser FROM Follow f WHERE f.followerUser.userId = :userId")
    Page<User> findFollowingsByUserId(@Param("userId") Long userId, Pageable pageable);

    // 특정 사용자가 하는 팔로잉에 대한 전체 삭제
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followerUser = :user")
    void deleteAllByFollowerUser(@Param("user") User user);

    // 특정 사용자를 팔로잉 중인 것에 대한 전체 삭제
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followingUser = :user")
    void deleteAllByFollowingUser(@Param("user") User user);
}
