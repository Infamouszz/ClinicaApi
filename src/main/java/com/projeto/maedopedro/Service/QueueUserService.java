package com.projeto.maedopedro.Service;


import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserResponseDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.QueueUserConfirmRequestDto;
import com.projeto.maedopedro.Dto.QueueUserDto.*;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import com.projeto.maedopedro.Model.QueueUserModel.QueueUser;
import com.projeto.maedopedro.Repository.QueueUserRepository;
import com.projeto.maedopedro.Specification.QueueUserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
//FALTA FAZER O MÉTODO PARA TRANSFORMAR EM LOYALTY USER E AGENDAR A CONSULTA
public class QueueUserService {
    private final QueueUserRepository queueUserRepository;

    //CREATE

    public QueueUserResponseDto createQueueUser(QueueUserRequestDto queueUserDto) {
        if (queueUserRepository.existsByEmail(queueUserDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        } else if (queueUserRepository.existsByPhoneNumber(queueUserDto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        QueueUser queueUser = QueueUser.builder()
                .email(queueUserDto.getEmail())
                .firstName(queueUserDto.getFirstName())
                .lastName(queueUserDto.getLastName())
                .phoneNumber(queueUserDto.getPhoneNumber())
                .build();
        QueueUser savedQueueUser = saveQueueUser(queueUser);
        return convertToResponseDto(savedQueueUser);
    }

    //DELETE
    @Transactional
    public void deleteQueueUserByEmail(String email) {
        QueueUser deletedQueueUser = queueUserRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("Queue user not found"));
        Long position = deletedQueueUser.getQueuePosition();
        queueUserRepository.delete(deletedQueueUser);
        queueUserRepository.decrementQueuePositionAfter(position);
    }

    //GETTER COM TODOS OS FILTROS
    public List<QueueUserResponseDto> searchQueueUsers(String firstName, String lastName, String email
            , String phoneNumber, Long queuePosition) {
        Specification<QueueUser> spec = Specification.unrestricted();
        if (StringUtils.hasText(firstName)) {
            spec = spec.and(QueueUserSpecification.hasFirstName(firstName));
        }
        if (StringUtils.hasText(lastName)) {
            spec = spec.and(QueueUserSpecification.hasLastName(lastName));
        }
        if (StringUtils.hasText(email)) {
            spec = spec.and(QueueUserSpecification.hasEmail(email));
        }
        if (StringUtils.hasText(phoneNumber)) {
            spec = spec.and(QueueUserSpecification.hasPhone(phoneNumber));
        }
        if (queuePosition != null) {
            spec = spec.and(QueueUserSpecification.hasQueuePosition(queuePosition));
        }
        List<QueueUser> queueUsers = queueUserRepository.findAll(spec);
        return queueUsers.stream().map(this::convertToResponseDto).toList();
    }
    public QueueUserResponseDto getQueueUserById(Long id) {
        QueueUser queueUser = queueUserRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Queue user not found"));
        return convertToResponseDto(queueUser);
    }

    //UPDATE DAS CREDENCIAIS
    @Transactional
    public QueueUserResponseDto updateQueueUser(String email, QueueUserPatchRequestDto patchDto) {
        QueueUser userToUpdate = queueUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(("User not found.")));
        if (StringUtils.hasText(patchDto.firstName())) {
            userToUpdate.setFirstName(patchDto.firstName());
        }
        if (StringUtils.hasText(patchDto.lastName())) {
            userToUpdate.setLastName(patchDto.lastName());
        }
        if (StringUtils.hasText(patchDto.email()) && !patchDto.email().equals(userToUpdate.getEmail())) {
            if (queueUserRepository.existsByEmail(patchDto.email())) {
                throw new IllegalArgumentException("Email already exists.");
            }
            userToUpdate.setEmail(patchDto.email());
        }
        if (StringUtils.hasText(patchDto.phoneNumber()) && !patchDto.phoneNumber().equals(userToUpdate.getPhoneNumber())) {
            if (queueUserRepository.existsByPhoneNumber(patchDto.phoneNumber())) {
                throw new IllegalArgumentException("Phone number already exists.");
            }
            userToUpdate.setPhoneNumber(patchDto.phoneNumber());
        }
        QueueUser savedUser = queueUserRepository.save(userToUpdate);
        return convertToResponseDto(savedUser);
    }

    //Métodos auxiliares
    private QueueUserResponseDto convertToResponseDto(QueueUser queueUser) {
        return new QueueUserResponseDto(
                queueUser.getId(),
                queueUser.getFirstName(),
                queueUser.getLastName(),
                queueUser.getEmail(),
                queueUser.getPhoneNumber(),
                queueUser.getQueuePosition(),
                queueUser.getCreatedAt()
        );
    }
    private QueueUser saveQueueUser(QueueUser newQueueUser) {
        if (newQueueUser.getId() == null) {
            newQueueUser.setCreatedAt(LocalDateTime.now());
            long currentQueueSize = queueUserRepository.count();
            newQueueUser.setQueuePosition(currentQueueSize + 1);
        }
        return queueUserRepository.save(newQueueUser);
    }
}
