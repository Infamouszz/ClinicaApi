package com.projeto.maedopedro.Controller;

import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserPatchDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserRequestDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserResponseDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.QueueUserConfirmRequestDto;
import com.projeto.maedopedro.Service.LoyaltyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loyalty-user")
@RequiredArgsConstructor
public class LoyaltyUserController {
    private final LoyaltyUserService loyaltyUserService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoyaltyUserResponseDto> createLoyaltyUser(@RequestBody LoyaltyUserRequestDto loyaltyUserRequestDto) {
        LoyaltyUserResponseDto createdUser = loyaltyUserService.createLoyaltUser(loyaltyUserRequestDto);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/delete/{cpf}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLoyaltyUser(@PathVariable String cpf) {
        loyaltyUserService.deleteLoyaltyUser(cpf);
    }

    @GetMapping
    public ResponseEntity<List<LoyaltyUserResponseDto>> getLoyaltyUsers(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String motherName,
            @RequestParam(required = false) String fatherName
    ) {
        List<LoyaltyUserResponseDto> loyaltyUsers = loyaltyUserService.searchLoyaltyUsers(cpf, firstName, lastName, email, phoneNumber,
                dateOfBirth, motherName, fatherName);
        return ResponseEntity.ok(loyaltyUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyUserResponseDto> getLoyaltyUserById(@PathVariable Long id) {
        LoyaltyUserResponseDto loyaltyUser = loyaltyUserService.getLoyaltyUserById(id);
        return ResponseEntity.ok(loyaltyUser);
    }

    @PostMapping("/confirm-queueUser/{id}")

    public ResponseEntity<LoyaltyUserResponseDto> confirmQueueUser(@PathVariable Long id
            , @RequestBody QueueUserConfirmRequestDto loyaltyUserRequestDto) {
        LoyaltyUserResponseDto confirmedQueueUser = loyaltyUserService.confirmQueueUser(id, loyaltyUserRequestDto);
        return ResponseEntity.ok(confirmedQueueUser);
    }

    @PatchMapping("/update/{cpf}")
    public ResponseEntity<LoyaltyUserResponseDto> updateLoyaltyUser(@PathVariable String cpf
            , @RequestBody LoyaltyUserPatchDto patchDto) {
        LoyaltyUserResponseDto updatedLoyaltyUser = loyaltyUserService.updateLoyaltyUser(cpf, patchDto);
        return ResponseEntity.ok(updatedLoyaltyUser);
    }
}