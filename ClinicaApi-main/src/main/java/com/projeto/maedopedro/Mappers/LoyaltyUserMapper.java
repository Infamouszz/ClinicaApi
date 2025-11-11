package com.projeto.maedopedro.Mappers;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserPatchDto;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface LoyaltyUserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchUserFromDto(LoyaltyUserPatchDto patchDto, @MappingTarget LoyaltyUser userToUpdate);
}
