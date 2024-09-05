package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.PlanCountProjection;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Modifying
    @Query("DELETE FROM Plan p WHERE p.planId = :planId")
    void deleteByPlanId(@Param("planId") Long planId);

    @Modifying
    @Query("DELETE FROM Plan p WHERE p.planUser = :user")
    void deleteAllByPlanUser(@Param("user") User user);

    Optional<Plan> findByPlanId(Long planId);

    @EntityGraph(attributePaths = "planUser")
    List<Plan> findAllByIsPublicTrue();

    @EntityGraph(attributePaths = "planUser")
    List<Plan> findAllByIsPublicTrueAndTitleContains(String keyword);
    List<Plan> findByPlanUser(User user);

    List<Plan> findAllByRecruitmentTrue();

    @Query("""
        SELECT p.planId as planId,
        (SELECT COUNT(b) FROM Bookmark b WHERE b.plan.planId = p.planId) as bookmarkCount,
        (SELECT COUNT(l) FROM Like l WHERE l.plan.planId = p.planId) as likeCount
        FROM Plan p WHERE p.planId IN :planIds
        """)
    List<PlanCountProjection> countBookmarksAndLikesByPlanIds(@Param("planIds") List<Long> planIds);

    // 플랜 조회 시 조회 수 증가
    @Modifying
    @Query("UPDATE Plan p SET p.viewCount = p.viewCount + 1 WHERE p.planId = :planId")
    void incrementViewCount(@Param("planId") Long planId);

    // 기본 인기순 조회 (Public, No Recruitment)
    @EntityGraph(attributePaths = "planUser")
    @Query("""
        SELECT p FROM Plan p
        LEFT JOIN Like l ON p.planId = l.plan.planId
        WHERE p.recruitment = false AND p.isPublic = true
        GROUP BY p.planId
        ORDER BY (p.viewCount * 0.075 + COUNT(l)) DESC
        """)
    Page<Plan> findPopularPlansNotInRecruitment(Pageable pageable);

    // 행정구역과 인기순 조회 (Public, No Recruitment)
    @EntityGraph(attributePaths = "planUser")
    @Query("""
        SELECT p FROM Plan p
        LEFT JOIN Like l ON p.planId = l.plan.planId
        WHERE p.recruitment = false AND p.isPublic = true
        AND p.frontLocation = :frontLocation
        GROUP BY p.planId
        ORDER BY (p.viewCount * 0.075 + COUNT(l)) DESC
        """)
    Page<Plan> findPopularPlansWithFrontLocation(@Param("frontLocation") String frontLocation, Pageable pageable);

    // 행정구역 + 도시와 인기순 조회 (Public, No Recruitment)
    @EntityGraph(attributePaths = "planUser")
    @Query("""
        SELECT p FROM Plan p
        LEFT JOIN Like l ON p.planId = l.plan.planId
        WHERE p.recruitment = false AND p.isPublic = true
        AND p.frontLocation = :frontLocation AND p.location = :location
        GROUP BY p.planId
        ORDER BY (p.viewCount * 0.075 + COUNT(l)) DESC
        """)
    Page<Plan> findPopularPlansWithAllLocation(@Param("frontLocation") String frontLocation,
                                               @Param("location") String location, Pageable pageable);

    // 기본 최신순 조회
    @EntityGraph(attributePaths = {"planUser", "planUsers"})
    @Query("""
        SELECT p FROM Plan p
        WHERE p.recruitment = :recruitment AND p.isPublic = true
        ORDER BY p.planId DESC
        """)
    Page<Plan> findRecentPlansByRecruitment(@Param("recruitment") Boolean recruitment, Pageable pageable);

    // 행정구역과 최신순 조회
    @EntityGraph(attributePaths = {"planUser", "planUsers"})
    @Query("""
        SELECT p FROM Plan p
        WHERE p.recruitment = :recruitment AND p.isPublic = true
        AND p.frontLocation = :frontLocation
        ORDER BY p.planId DESC
        """)
    Page<Plan> findRecentPlansWithFrontLocation(@Param("frontLocation") String frontLocation, @Param("recruitment") Boolean recruitment, Pageable pageable);

    // 행정구역 + 도시와 최신순 조회
    @EntityGraph(attributePaths = {"planUser", "planUsers"})
    @Query("""
        SELECT p FROM Plan p
        WHERE p.recruitment = :recruitment AND p.isPublic = true
        AND p.frontLocation = :frontLocation AND p.location = :location
        ORDER BY p.planId DESC
        """)
    Page<Plan> findRecentPlansWithAllLocation(@Param("frontLocation") String frontLocation,
                                              @Param("location") String location,
                                              @Param("recruitment") Boolean recruitment, Pageable pageable);

    // 여행 시작 날짜, 최신순 조회 (Public, Recruitment)
    @EntityGraph(attributePaths = {"planUser", "planUsers"})
    @Query("""
        SELECT p FROM Plan p
        WHERE p.recruitment = :recruitment AND p.isPublic = true
        AND p.startDate = :startDate
        ORDER BY p.planId DESC
        """)
    Page<Plan> findRecentPlansRecruitmentByStartDate(@Param("recruitment") Boolean recruitment,
                                                     @Param("startDate") LocalDate startDate, Pageable pageable);
    // 여행 시작 날짜, 행정구역과 최신순 조회
    @EntityGraph(attributePaths = {"planUser", "planUsers"})
    @Query("""
        SELECT p FROM Plan p
        WHERE p.recruitment = :recruitment AND p.isPublic = true
        AND p.frontLocation = :frontLocation AND p.startDate = :startDate
        ORDER BY p.planId DESC
        """)
    Page<Plan> findRecentPlansWithFrontLocationAndStartDate(@Param("frontLocation") String frontLocation,
                                                            @Param("recruitment") Boolean recruitment,
                                                            @Param("startDate") LocalDate startDate, Pageable pageable);
    // 여행 시작 날짜, 행정구역 + 도시와 최신순 조회
    @EntityGraph(attributePaths = {"planUser", "planUsers"})
    @Query("""
        SELECT p FROM Plan p
        WHERE p.recruitment = :recruitment AND p.isPublic = true
        AND p.frontLocation = :frontLocation AND p.location = :location AND p.startDate = :startDate
        ORDER BY p.planId DESC
        """)
    Page<Plan> findRecentPlansWithAllLocationAndStartDate(@Param("frontLocation") String frontLocation,
                                                          @Param("location") String location,
                                                          @Param("recruitment") Boolean recruitment,
                                                          @Param("startDate") LocalDate startDate, Pageable pageable);
}
