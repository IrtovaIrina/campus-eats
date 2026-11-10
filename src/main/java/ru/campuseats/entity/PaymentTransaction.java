package ru.campuseats.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.campuseats.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private FoodOrder order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, unique = true, length = 80)
    private String transactionReference;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal bonusUsed = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal cashbackEarned = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "SUCCESS";

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime paidAt = LocalDateTime.now();
}
