package com.projeto.maedopedro.Mapper;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserPatchRequestDto;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import org.mapstruct.*;
;

@Mapper(componentModel = "spring")
public interface LoyaltyUserMapper {

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchUserFromDto(LoyaltyUserPatchRequestDto patchDto, @MappingTarget LoyaltyUser userToUpdate);
}
