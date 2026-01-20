package com.projeto.maedopedro.Dto.AppointmentDto;

import com.projeto.maedopedro.Model.AppointmentModel.Status;
import com.projeto.maedopedro.Model.LolyaltUsersModel.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentSearchRequestDto {
    Status status;
    LocalDateTime dateAppointment;
    String firstName;
    String lastName;
    Gender gender;
    String phoneNumber;
    String cpf;
}
