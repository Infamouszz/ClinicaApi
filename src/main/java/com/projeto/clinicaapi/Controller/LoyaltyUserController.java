package com.projeto.clinicaapi.Controller;

import com.projeto.clinicaapi.Dto.LoyaltUserDto.*;
import com.projeto.clinicaapi.Service.LoyaltyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<LoyaltyUserResponseDto>> getLoyaltyUsers(LoyaltyUserSearchRequestDto loyaltyUserSearchRequestDto) {
        List<LoyaltyUserResponseDto> loyaltyUsers = loyaltyUserService.searchLoyaltyUsers(loyaltyUserSearchRequestDto);
        return ResponseEntity.ok(loyaltyUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyUserResponseDto> getLoyaltyUserById(@PathVariable Long id) {
        LoyaltyUserResponseDto loyaltyUser = loyaltyUserService.getLoyaltyUserById(id);
        return ResponseEntity.ok(loyaltyUser);
    }

    @PostMapping("/confirm-queueUser/{id}")

    public ResponseEntity<LoyaltyUserResponseDto> confirmQueueUser(@PathVariable Long id
            , @RequestBody LoyaltyUserQueueUserConfimRequestDto loyaltyUserRequestDto) {
        LoyaltyUserResponseDto confirmedQueueUser = loyaltyUserService.confirmQueueUser(id, loyaltyUserRequestDto);
        return ResponseEntity.ok(confirmedQueueUser);
    }

    @PatchMapping("/update/{cpf}")
    public ResponseEntity<LoyaltyUserResponseDto> updateLoyaltyUser(@PathVariable String cpf
            , @RequestBody LoyaltyUserPatchRequestDto patchDto) {
        LoyaltyUserResponseDto updatedLoyaltyUser = loyaltyUserService.updateLoyaltyUser(cpf, patchDto);
        return ResponseEntity.ok(updatedLoyaltyUser);
    }
}