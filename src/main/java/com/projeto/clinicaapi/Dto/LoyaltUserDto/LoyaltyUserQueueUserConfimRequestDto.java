package com.projeto.clinicaapi.Dto.LoyaltUserDto;

import com.projeto.clinicaapi.Model.LolyaltUsersModel.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyUserQueueUserConfimRequestDto {
    String cpf;
    LocalDate dateOfBirth;
    Gender gender;
    String motherName;
    String fatherName;
}
