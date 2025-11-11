package com.projeto.maedopedro.Controller;


import com.projeto.maedopedro.Dto.QueueUserDto.QueueUserRequestDto;
import com.projeto.maedopedro.Dto.QueueUserDto.QueueUserResponseDto;
import com.projeto.maedopedro.Dto.QueueUserDto.QueueUserPatchRequestDto;
import com.projeto.maedopedro.Service.QueueUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queue-user")
@RequiredArgsConstructor
public class QueueUserController {
    private final QueueUserService queueUserService;

    //Create padrão, precisa enviar o formato do json correto senão ele da forbidden :)
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public QueueUserResponseDto createQueueUser(@RequestBody QueueUserRequestDto queueUser){
        return queueUserService.createQueueUser(queueUser);
    }

    //Delete, necessário fornecer o email
    @DeleteMapping("/delete/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQueueUser(@PathVariable String email){
        queueUserService.deleteQueueUserByEmail(email);
    }

    //Métodos getters
    @GetMapping
    public ResponseEntity<List<QueueUserResponseDto>> getQueueUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Long queuePosition
    ){
        List<QueueUserResponseDto> queueUsers = queueUserService.searchQueueUsers(firstName, lastName, email, phoneNumber, queuePosition);
        return ResponseEntity.ok(queueUsers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<QueueUserResponseDto> getQueueUserById(@PathVariable Long id){
        QueueUserResponseDto queueUser = queueUserService.getQueueUserById(id);
        return ResponseEntity.ok(queueUser);
    }

    //Updates
    @PatchMapping("/update/{email}")
    public ResponseEntity<QueueUserResponseDto> updateQueueUserCredentials(
            @PathVariable String email,
            @Valid @RequestBody QueueUserPatchRequestDto patchRequestDto){
         return ResponseEntity.ok(queueUserService.updateQueueUser(email,patchRequestDto));
    }
}
