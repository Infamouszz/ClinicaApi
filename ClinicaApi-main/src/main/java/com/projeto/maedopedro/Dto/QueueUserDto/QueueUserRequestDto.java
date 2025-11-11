package com.projeto.maedopedro.Dto.QueueUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueueUserRequestDto {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Long queuePosition;
}
