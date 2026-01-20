package com.projeto.clinicaapi.Mapper;

import com.projeto.clinicaapi.Dto.QueueUserDto.QueueUserPatchRequestDto;
import com.projeto.clinicaapi.Model.QueueUserModel.QueueUser;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface QueueUserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchUserFromDto(QueueUserPatchRequestDto patchDto, @MappingTarget QueueUser userToUpdate);
}
