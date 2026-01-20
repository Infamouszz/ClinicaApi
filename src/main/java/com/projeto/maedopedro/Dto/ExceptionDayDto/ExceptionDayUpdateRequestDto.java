package com.projeto.maedopedro.Dto.ExceptionDayDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDayUpdateRequestDto {
    LocalDateTime startTime;
    LocalDateTime endTime;
    String reason;
}
