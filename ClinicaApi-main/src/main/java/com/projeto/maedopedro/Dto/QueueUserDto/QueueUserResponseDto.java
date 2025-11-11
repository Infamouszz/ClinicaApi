package com.projeto.maedopedro.Dto.QueueUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueueUserResponseDto {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Long queuePosition;
    LocalDateTime createdAt;
}
