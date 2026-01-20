package com.projeto.maedopedro.Dto.OfficeHourDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeHourDayResponseDto {
    private LocalDate day;
    private LocalTime availableHours;
}
