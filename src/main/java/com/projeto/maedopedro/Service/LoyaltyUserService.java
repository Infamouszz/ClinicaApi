package com.projeto.maedopedro.Service;
import com.projeto.maedopedro.Dto.LoyaltUserDto.*;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceAlreadyExistsException;
import com.projeto.maedopedro.Mapper.LoyaltyUserMapper;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import com.projeto.maedopedro.Model.QueueUserModel.QueueUser;
import com.projeto.maedopedro.Repository.LoyaltyUserRepository;
import com.projeto.maedopedro.Repository.QueueUserRepository;
import com.projeto.maedopedro.Specification.LoyaltyUserSpecification;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoyaltyUserService {

    private final LoyaltyUserRepository loyaltyUserRepository;
    private final FileStorageService fileStorageService;
    private final QueueUserRepository queueUserRepository;
    private final LoyaltyUserMapper loyaltyUserMapper;

    public LoyaltyUserResponseDto createLoyaltUser(LoyaltyUserRequestDto loyaltyUserDto) {
        if (loyaltyUserRepository.existsLoyaltyUserByCpf((loyaltyUserDto.getCpf()))){
            throw new ResourceAlreadyExistsException("Loyalty User already exists");
        }else if (loyaltyUserRepository.existsLoyaltyUserByPhoneNumber((loyaltyUserDto.getPhoneNumber()))){
            throw new ResourceAlreadyExistsException("Loyalty User with phone number already exists");
        }else if(loyaltyUserRepository.existsLoyaltyUserByEmail((loyaltyUserDto.getEmail()))){
            throw new ResourceAlreadyExistsException("Loyalty User with email already exists");
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
                .anamnesePdfPath("None anamnese yet")
                .build();
        LoyaltyUser savedLoyaltyUser = saveLoyaltUser(loyaltyUser);
        return convertToResponseDto(savedLoyaltyUser);
    }

    //DELETE
    @Transactional
    public void deleteLoyaltyUser(String cpf) {
        LoyaltyUser deletedUser = loyaltyUserRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyUser not found with cpf: " + cpf));
        loyaltyUserRepository.delete(deletedUser);
    }

    //GETTER (SEARCH)
    public List<LoyaltyUserResponseDto> searchLoyaltyUsers(LoyaltyUserSearchRequestDto loyaltyUserSearchRequestDto) {
        Specification<LoyaltyUser> spec = Specification.allOf(LoyaltyUserSpecification.hasCpf(loyaltyUserSearchRequestDto.getCpf()),
        LoyaltyUserSpecification.hasFirstName(loyaltyUserSearchRequestDto.getFirstName()),
        LoyaltyUserSpecification.hasLastName(loyaltyUserSearchRequestDto.getLastName()),
        LoyaltyUserSpecification.hasEmail(loyaltyUserSearchRequestDto.getEmail()),
        LoyaltyUserSpecification.hasPhone(loyaltyUserSearchRequestDto.getPhoneNumber()),
        LoyaltyUserSpecification.hasDateOfBirth(loyaltyUserSearchRequestDto.getDateOfBirth()),
        LoyaltyUserSpecification.hasMotherName(loyaltyUserSearchRequestDto.getMotherName()),
        LoyaltyUserSpecification.hasFatherName(loyaltyUserSearchRequestDto.getFatherName()));
        List<LoyaltyUser> loyaltyUsers = loyaltyUserRepository.findAll(spec);
        return loyaltyUsers.stream().map(this::convertToResponseDto).toList();
    }
    public LoyaltyUserResponseDto getLoyaltyUserById(Long id) {
        LoyaltyUser loyaltyUser = loyaltyUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyUser not found with id: " + id));
        return convertToResponseDto(loyaltyUser);
    }

    @Transactional
    public LoyaltyUserResponseDto updateLoyaltyUser(String cpf, LoyaltyUserPatchRequestDto loyaltyUserPatchRequestDto){
        LoyaltyUser userToUpdate = loyaltyUserRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyUser not found with cpf: " + cpf));
        loyaltyUserMapper.patchUserFromDto(loyaltyUserPatchRequestDto, userToUpdate);
        LoyaltyUser savedUser = loyaltyUserRepository.save(userToUpdate);
        return convertToResponseDto(savedUser);
    }

    @Transactional
    public void saveAnamnesePathToLoyaltyUser(String anamnesePath, Long loyaltyUserId) {
        LoyaltyUser loyaltyuser = loyaltyUserRepository.findById(loyaltyUserId)
                .orElseThrow(()-> new ResourceNotFoundException("LoyaltyUser not found with id: " + loyaltyUserId));
        loyaltyuser.setAnamnesePdfPath(anamnesePath);
        loyaltyUserRepository.save(loyaltyuser);
    }
    public Resource getAnamense(Long loyaltyUserId) {
        LoyaltyUser loyaltyUser = loyaltyUserRepository.findById(loyaltyUserId)
                .orElseThrow(()-> new ResourceNotFoundException("LoyaltyUser not found with id: " + loyaltyUserId));
        String fullPath = loyaltyUser.getAnamnesePdfPath();
        if (fullPath == null || fullPath.isEmpty()) {
            throw new ResourceNotFoundException("Anamnese path not found");
        }
        if (fullPath.equals("None anamnese yet")){
            throw new ResourceNotFoundException("User doesn't have anamnese yet");
        }
        String filename = Paths.get(fullPath).getFileName().toString();
        return fileStorageService.loadFile(filename);
    }

    @Transactional
    public LoyaltyUserResponseDto confirmQueueUser(Long queueUserId, LoyaltyUserQueueUserConfimRequestDto queueUserRequestDto) {
        QueueUser queueUser = queueUserRepository.findById(queueUserId)
                .orElseThrow(()-> new ResourceNotFoundException("QueueUser not found with id: " + queueUserId));
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
