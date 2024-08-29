package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.VoteRequestDto;
import org.example.todotravel.domain.plan.dto.response.VoteResponseDto;
import org.example.todotravel.domain.plan.entity.Location;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.entity.Vote;
import org.example.todotravel.domain.plan.repository.VoteRepository;
import org.example.todotravel.domain.plan.service.LocationService;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.VoteService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService{
    private final VoteRepository voteRepository;
    private final PlanService planService;
    private final UserService userService;
    private final LocationService locationService;
    private final JwtTokenizer jwtTokenizer;

    //투표 생성
    @Override
    @Transactional
    public Vote createVote(Long planId, User user, VoteRequestDto dto) {
        Plan plan = planService.getPlan(planId);

        Location location = locationService.findByLocationId(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("장소 등록이 안된 요청입니다."));

        PlanUser planUser = plan.getPlanUsers().stream()
                .filter(pu -> pu.getUser().equals(user) && pu.getStatus() == PlanUser.StatusType.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("플랜에 참여하지 않았거나 승인이 되지 않은 사용자입니다."));

        Vote vote = Vote.builder()
                .plan(plan)
                .location(location)
                .planUser(planUser)
                .voteCount(0)
                .startDate(LocalDateTime.now())
                .endDate(dto.getEndDate())
                .category(dto.getCategory())
                .build();

        return voteRepository.save(vote);
    }

    //투표 수정 (종류일, 카테고리)
    @Override
    @Transactional
    public Vote updateVote(Long planId, Long voteId, User user, VoteRequestDto dto) {
        Plan plan = planService.getPlan(planId);
        PlanUser planUser = plan.getPlanUsers().stream()
                .filter(pu -> pu.getUser().equals(user) && pu.getStatus() == PlanUser.StatusType.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("플랜에 참여하지 않았거나 승인이 되지 않은 사용자입니다."));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("해당하는 투표가 없습니다."));

        vote.setEndDate(dto.getEndDate());
        vote.setCategory(dto.getCategory());

        return voteRepository.save(vote);
    }

    //투표 삭제
    @Override
    @Transactional
    public void deleteVote(Long planId, User user) {
        Plan plan = planService.getPlan(planId);
        PlanUser planUser = plan.getPlanUsers().stream()
                .filter(pu -> pu.getUser().equals(user) && pu.getStatus() == PlanUser.StatusType.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("플랜에 참여하지 않았거나 승인이 되지 않은 사용자입니다."));

        List<Vote> votes = voteRepository.findAllByPlanAndPlanUser(plan, planUser);
        voteRepository.deleteAll(votes);
    }

    //투표 전체 보기
    @Override
    @Transactional(readOnly = true)
    public Page<VoteResponseDto> getAllVoteList(Long planId, Pageable pageable) {
        Plan plan = planService.getPlan(planId);
        Page<Vote> votePage = voteRepository.findAllByPlan(plan, pageable);

        return votePage.map(vote -> VoteResponseDto.builder()
                .voteId(vote.getVoteId())
                .locationId(vote.getLocation().getLocationId())
                .voteCount(vote.getVoteCount())
                .startDate(vote.getStartDate())
                .endDate(vote.getEndDate())
                .category(vote.getCategory())
                .build());
    }
    //투표 단일 보기
    @Override
    @Transactional(readOnly = true)
    public Vote getVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("해당 투표가 존재하지 않습니다."));
    }
}
