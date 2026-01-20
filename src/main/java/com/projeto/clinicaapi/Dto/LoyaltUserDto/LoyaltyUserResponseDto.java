package com.projeto.clinicaapi.Dto.LoyaltUserDto;

import com.projeto.clinicaapi.Model.LolyaltUsersModel.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyUserResponseDto {
    private Long id;
    private String cpf;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String motherName;
    private String fatherName;
    private LocalDateTime createdAt;
}
