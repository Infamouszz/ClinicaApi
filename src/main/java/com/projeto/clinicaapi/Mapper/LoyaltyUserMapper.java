package com.projeto.clinicaapi.Mapper;
import com.projeto.clinicaapi.Dto.LoyaltUserDto.LoyaltyUserPatchRequestDto;
import com.projeto.clinicaapi.Model.LolyaltUsersModel.LoyaltyUser;
import org.mapstruct.*;
;

@Mapper(componentModel = "spring")
public interface LoyaltyUserMapper {

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchUserFromDto(LoyaltyUserPatchRequestDto patchDto, @MappingTarget LoyaltyUser userToUpdate);
}
