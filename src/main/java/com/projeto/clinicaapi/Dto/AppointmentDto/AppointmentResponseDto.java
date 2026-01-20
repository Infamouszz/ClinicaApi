package com.projeto.clinicaapi.Dto.AppointmentDto;

import com.projeto.clinicaapi.Model.AppointmentModel.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentResponseDto {
    private Long id;
    private LocalDateTime appointmentDate;
    private Status status;
    private LocalDateTime createdAt;
    private AppointmentLoyaltyUserRequestDto loyaltyUser;
    private String observation;
}
