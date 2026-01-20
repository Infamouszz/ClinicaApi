package com.projeto.maedopedro.Dto.AppointmentDto;

import com.projeto.maedopedro.Model.LolyaltUsersModel.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentLoyaltyUserRequestDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String motherName;
    private String fatherName;
    private Gender gender;
}
