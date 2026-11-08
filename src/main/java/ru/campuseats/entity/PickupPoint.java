package ru.campuseats.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pickup_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;

    @Column(nullable = false, length = 50)
    private String windowCode;

    @Column(nullable = false, length = 250)
    private String locationHint;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxOrdersPerSlot = 15;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}