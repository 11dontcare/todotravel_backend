package org.example.todotravel.domain.plan.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id", nullable = false)
    private Long budgetId;

    @Column(name = "item", length = 100)
    private String item;

    @Column(name = "price")
    private Long price;
}
