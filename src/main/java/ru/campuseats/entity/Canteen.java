package ru.campuseats.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "canteens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Canteen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String building;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false, length = 50)
    private String openingHours;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Double rating = 5.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reviewsCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isOpen = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;
}