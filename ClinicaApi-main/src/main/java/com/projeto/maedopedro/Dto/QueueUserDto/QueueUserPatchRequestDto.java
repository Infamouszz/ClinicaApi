package com.projeto.maedopedro.Dto.QueueUserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record QueueUserPatchRequestDto(@Email(message="Invalid email format.") String email,
                                       String firstName,
                                       String lastName,
                                       @Pattern(regexp = "^\\d{10,11}$",message = "Invalid phone format")String phoneNumber,
                                       Long queuePosition) {
}
