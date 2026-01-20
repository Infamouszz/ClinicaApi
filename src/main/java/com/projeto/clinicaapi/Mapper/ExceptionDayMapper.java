package com.projeto.clinicaapi.Mapper;

import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayUpdateRequestDto;
import com.projeto.clinicaapi.Model.ScheduleModel.ExceptionDay;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ExceptionDayMapper {
    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchExceptionDay(ExceptionDayUpdateRequestDto exceptionDayUpdateRequestDto, @MappingTarget ExceptionDay exceptionDayToUpdate);
}
