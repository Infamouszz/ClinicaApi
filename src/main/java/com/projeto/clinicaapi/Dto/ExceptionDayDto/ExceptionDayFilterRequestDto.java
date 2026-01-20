package com.projeto.clinicaapi.Dto.ExceptionDayDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDayFilterRequestDto {
    String startTime;
    String endTime;
}
