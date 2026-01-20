package com.projeto.maedopedro.Service;


import com.projeto.maedopedro.Dto.QueueUserDto.*;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceAlreadyExistsException;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceNotFoundException;
import com.projeto.maedopedro.Mapper.QueueUserMapper;
import com.projeto.maedopedro.Model.QueueUserModel.QueueUser;
import com.projeto.maedopedro.Repository.QueueUserRepository;
import com.projeto.maedopedro.Specification.QueueUserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class    QueueUserService {
    private final QueueUserRepository queueUserRepository;
    private final QueueUserMapper queueUserMapper;

    //CREATE
    public QueueUserResponseDto createQueueUser(QueueUserCreateRequestDto queueUserDto) {
        if (queueUserRepository.existsByEmail(queueUserDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Queue user with email already exists");
        } else if (queueUserRepository.existsByPhoneNumber(queueUserDto.getPhoneNumber())) {
            throw new ResourceAlreadyExistsException("Queue user with phone number already exists");
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
                        .orElseThrow(() -> new ResourceNotFoundException("Queue User not found with email: " + email));
        Long position = deletedQueueUser.getQueuePosition();
        queueUserRepository.delete(deletedQueueUser);
        queueUserRepository.decrementQueuePositionAfter(position);
    }

    //GETTER COM TODOS OS FILTROS
    public List<QueueUserResponseDto> searchQueueUsers(QueueUserSearchRequestDto queueUserSearchRequestDto) {
        Specification<QueueUser> spec = Specification.allOf(QueueUserSpecification.hasFirstName(queueUserSearchRequestDto.getFirstName()),
                QueueUserSpecification.hasLastName(queueUserSearchRequestDto.getLastName()),
                QueueUserSpecification.hasEmail(queueUserSearchRequestDto.getEmail()),
                QueueUserSpecification.hasPhone(queueUserSearchRequestDto.getPhoneNumber()),
                QueueUserSpecification.hasQueuePosition(queueUserSearchRequestDto.getQueuePosition()));
        List<QueueUser> queueUsers = queueUserRepository.findAll(spec);
        return queueUsers.stream().map(this::convertToResponseDto).toList();
    }
    public QueueUserResponseDto getQueueUserById(Long id) {
        QueueUser queueUser = queueUserRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Queue User not found with id: " + id));
        return convertToResponseDto(queueUser);
    }

    //UPDATE DAS CREDENCIAIS
    @Transactional
    public QueueUserResponseDto updateQueueUser(String email, QueueUserPatchRequestDto patchDto) {
        QueueUser userToUpdate = queueUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Queue User not found with email: " + email));
        queueUserMapper.patchUserFromDto(patchDto,userToUpdate);
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
