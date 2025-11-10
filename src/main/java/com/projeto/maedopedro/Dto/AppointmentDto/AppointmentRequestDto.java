package com.projeto.maedopedro.Dto.AppointmentDto;

import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserAppointmentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequestDto {
    private LocalDateTime appointmentDate;
    private String cpf;
    private String observation;
}
