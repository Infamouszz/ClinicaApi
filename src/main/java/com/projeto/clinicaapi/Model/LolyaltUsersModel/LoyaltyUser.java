package com.projeto.clinicaapi.Model.LolyaltUsersModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoyaltyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loyaltyUserId;

    @Column(unique= true, nullable=false)
    private String cpf;

    @Column(nullable= false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true)
    private String motherName;

    @Column(nullable = true)
    private String fatherName;

    @Column(nullable = true)
    private String anamnesePdfPath;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
