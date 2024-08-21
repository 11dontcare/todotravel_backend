package org.example.todotravel.domain.plan.dto.response;

import java.time.LocalDate;

public interface PlanSummaryDto {
    Long getPlanId();
    String getTitle();
    String getLocation();
    String getDescription();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getPlanUserNickname();
}
