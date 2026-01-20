package com.projeto.maedopedro.Dto.QueueUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class QueueUserSearchRequestDto {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Long queuePosition;
}
