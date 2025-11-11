package com.projeto.maedopedro.Service;

import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentResponseDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserPatchDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserRequestDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserResponseDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.QueueUserConfirmRequestDto;
import com.projeto.maedopedro.Dto.QueueUserDto.QueueUserResponseDto;
import com.projeto.maedopedro.Mappers.LoyaltyUserMapper;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import com.projeto.maedopedro.Model.QueueUserModel.QueueUser;
import com.projeto.maedopedro.Repository.LoyaltyUserRepository;
import com.projeto.maedopedro.Repository.QueueUserRepository;
import com.projeto.maedopedro.Specification.LoyaltyUserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
//FALTA FAZER O UPDATE (TO COM PREGUIÇA)
//FALTA FAZER O MÉTODO PARA COLOCAR O PDF
public class LoyaltyUserService {

    private final LoyaltyUserRepository loyaltyUserRepository;
    private final FileStorageService fileStorageService;
    private final QueueUserRepository queueUserRepository;
    private final LoyaltyUserMapper loyaltyUserMapper;

    //CREATE, IREI FAZER A PARTE DO PDF POR ÚLTIMO, PENSO EM CRIAR UM MÉTODO COMPACTADOR ANTES DE SALVAR O CAMINHO
    public LoyaltyUserResponseDto createLoyaltUser(LoyaltyUserRequestDto loyaltyUserDto) {
        if (loyaltyUserRepository.existsLoyaltyUserByCpf((loyaltyUserDto.getCpf()))){
            throw new IllegalArgumentException("CPF already exists");
        }else if (loyaltyUserRepository.existsLoyaltyUserByPhoneNumber((loyaltyUserDto.getPhoneNumber()))){
            throw new IllegalArgumentException("Phone number already exists");
        }else if(loyaltyUserRepository.existsLoyaltyUserByEmail((loyaltyUserDto.getEmail()))){
            throw new IllegalArgumentException("Email already exists");
        }
        LoyaltyUser loyaltyUser = LoyaltyUser.builder()
                .cpf(loyaltyUserDto.getCpf())
                .firstName(loyaltyUserDto.getFirstName())
                .lastName(loyaltyUserDto.getLastName())
                .email(loyaltyUserDto.getEmail())
                .phoneNumber(loyaltyUserDto.getPhoneNumber())
                .dateOfBirth(loyaltyUserDto.getDateOfBirth())
                .gender(loyaltyUserDto.getGender())
                .motherName(loyaltyUserDto.getMotherName())
                .fatherName(loyaltyUserDto.getFatherName())
                .anamnesePdfPath("W.I.P")
                .build();
        LoyaltyUser savedLoyaltyUser = saveLoyaltUser(loyaltyUser);
        return convertToResponseDto(savedLoyaltyUser);
    }

    //DELETE
    @Transactional
    public void deleteLoyaltyUser(String cpf) {
        LoyaltyUser deletedUser = loyaltyUserRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("User not found") );
        loyaltyUserRepository.delete(deletedUser);
    }

    //GETTER (SEARCH)
    public List<LoyaltyUserResponseDto> searchLoyaltyUsers(String cpf, String firstName
            , String lastName, String email, String phoneNumber, LocalDate dateOfBirth, String motherName, String fatherName) {
        Specification<LoyaltyUser> spec = Specification.unrestricted();
        if(StringUtils.hasText(cpf)){
            spec = spec.and(LoyaltyUserSpecification.hasCpf(cpf));
        }
        if (StringUtils.hasText(firstName)) {
            spec = spec.and(LoyaltyUserSpecification.hasFirstName(firstName));
        }
        if (StringUtils.hasText(lastName)) {
            spec = spec.and(LoyaltyUserSpecification.hasLastName(lastName));
        }
        if (StringUtils.hasText(email)) {
            spec = spec.and(LoyaltyUserSpecification.hasEmail(email));
        }
        if (StringUtils.hasText(phoneNumber)) {
            spec = spec.and(LoyaltyUserSpecification.hasPhone(phoneNumber));
        }
        if (dateOfBirth != null) {
            spec = spec.and(LoyaltyUserSpecification.hasDateOfBirth(dateOfBirth));
        }
        if (StringUtils.hasText(motherName)) {
            spec = spec.and(LoyaltyUserSpecification.hasMotherName(motherName));
        }
        if (StringUtils.hasText(fatherName)) {
            spec = spec.and(LoyaltyUserSpecification.hasFatherName(fatherName));
        }
        List<LoyaltyUser> loyaltyUsers = loyaltyUserRepository.findAll(spec);
        return loyaltyUsers.stream().map(this::convertToResponseDto).toList();
    }
    public LoyaltyUserResponseDto getLoyaltyUserById(Long id) {
        LoyaltyUser loyaltyUser = loyaltyUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return convertToResponseDto(loyaltyUser);
    }

    //UPDATE (W.I.P)
    @Transactional
    public LoyaltyUserResponseDto updateLoyaltyUser(String cpf, LoyaltyUserPatchDto loyaltyUserPatchDto){
        LoyaltyUser userToUpdate = loyaltyUserRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        loyaltyUserMapper.patchUserFromDto(loyaltyUserPatchDto, userToUpdate);
        LoyaltyUser savedUser = loyaltyUserRepository.save(userToUpdate);
        return convertToResponseDto(savedUser);
    }

    @Transactional
    public void saveAnamnesePathToLoyaltyUser(String anamnesePath, Long loyaltyUserId) {
        LoyaltyUser loyaltyuser = loyaltyUserRepository.findById(loyaltyUserId)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        loyaltyuser.setAnamnesePdfPath(anamnesePath);
        loyaltyUserRepository.save(loyaltyuser);
    }
    public Resource getAnamense(Long loyaltyUserId) {
        LoyaltyUser loyaltyUser = loyaltyUserRepository.findById(loyaltyUserId)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        String fullPath = loyaltyUser.getAnamnesePdfPath();
        if (fullPath == null || fullPath.isEmpty()) {
            throw new EntityNotFoundException("AnamnesePdfPath not found");
        }
        String filename = Paths.get(fullPath).getFileName().toString();
        return fileStorageService.loadFile(filename);
    }

    @Transactional
    public LoyaltyUserResponseDto confirmQueueUser(Long queueUserId, QueueUserConfirmRequestDto queueUserRequestDto) {
        QueueUser queueUser = queueUserRepository.findById(queueUserId)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        LoyaltyUser loyaltyUser = LoyaltyUser.builder()
                .firstName(queueUser.getFirstName())
                .lastName(queueUser.getLastName())
                .email(queueUser.getEmail())
                .phoneNumber(queueUser.getPhoneNumber())
                .cpf(queueUserRequestDto.getCpf())
                .dateOfBirth(queueUserRequestDto.getDateOfBirth())
                .gender(queueUserRequestDto.getGender())
                .motherName(queueUserRequestDto.getMotherName())
                .fatherName(queueUserRequestDto.getFatherName())
                .build();
        LoyaltyUser savedLoyaltyUser = saveLoyaltUser(loyaltyUser);
        return convertToResponseDto(savedLoyaltyUser);
    }

    //MÉTODOS AUXILIARES
    private LoyaltyUser saveLoyaltUser(LoyaltyUser newLoyaltyUser) {
            if (newLoyaltyUser.getLoyaltyUserId() == null) {
                newLoyaltyUser.setCreatedAt(LocalDateTime.now());
            }
            return loyaltyUserRepository.save(newLoyaltyUser);
    }
    private LoyaltyUserResponseDto convertToResponseDto(LoyaltyUser loyaltyUser) {
        return new LoyaltyUserResponseDto(
                loyaltyUser.getLoyaltyUserId(),
                loyaltyUser.getCpf(),
                loyaltyUser.getFirstName(),
                loyaltyUser.getLastName(),
                loyaltyUser.getEmail(),
                loyaltyUser.getPhoneNumber(),
                loyaltyUser.getDateOfBirth(),
                loyaltyUser.getGender(),
                loyaltyUser.getMotherName(),
                loyaltyUser.getFatherName(),
                loyaltyUser.getCreatedAt()
        );
    }
}
