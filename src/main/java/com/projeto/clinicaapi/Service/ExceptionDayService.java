package com.projeto.clinicaapi.Service;
import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayCreateRequestDto;
import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayFilterRequestDto;
import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayUpdateRequestDto;
import com.projeto.clinicaapi.ExceptionHandler.Exception.BusinessLogicException;
import com.projeto.clinicaapi.ExceptionHandler.Exception.ResourceNotFoundException;
import com.projeto.clinicaapi.Mapper.ExceptionDayMapper;
import com.projeto.clinicaapi.Model.ScheduleModel.ExceptionDay;
import com.projeto.clinicaapi.Repository.ExceptionDayRepository;
import com.projeto.clinicaapi.Specification.ExceptionDaySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExceptionDayService {
    private final ExceptionDayRepository exceptionDayRepository;
    private final ExceptionDayMapper exceptionDayMapper;

    public ExceptionDay createExceptionDays(ExceptionDayCreateRequestDto exceptionDayRequest) {
        if (exceptionDayRequest.getStartTime().equals(exceptionDayRequest.getEndTime()) ||
        exceptionDayRequest.getStartTime().isAfter(exceptionDayRequest.getEndTime())) {
            throw new BusinessLogicException("Start time cannot be after or equals end time");
        }
        ExceptionDay exceptionDay = ExceptionDay.builder()
                .startTime(exceptionDayRequest.getStartTime())
                .endTime(exceptionDayRequest.getEndTime())
                .reason(exceptionDayRequest.getReason())
                .build();
        ExceptionDay savedExceptionDay = saveExceptionDay(exceptionDay);
        return exceptionDayRepository.save(savedExceptionDay);
    }

    public void deleteExceptionDays(Long id) {
        ExceptionDay deletedExceptionDay = exceptionDayRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Exception not found with id: " + id));
        exceptionDayRepository.delete(deletedExceptionDay);
    }

    @Transactional
    public ExceptionDay updateExceptionDays(ExceptionDayUpdateRequestDto exceptionDayUpdateRequestDto, Long id) {
        if (exceptionDayUpdateRequestDto.getStartTime().equals(exceptionDayUpdateRequestDto.getEndTime()) ||
                exceptionDayUpdateRequestDto.getStartTime().isAfter(exceptionDayUpdateRequestDto.getEndTime())) {
            throw new BusinessLogicException("Start time cannot be after or equals end time");
        }
        ExceptionDay exceptionDayToUpdate = exceptionDayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exception not found with id: " + id));
        exceptionDayMapper.patchExceptionDay(exceptionDayUpdateRequestDto, exceptionDayToUpdate);
        ExceptionDay updatedExceptionDay = exceptionDayRepository.save(exceptionDayToUpdate);
        return updatedExceptionDay;
    }

    public List<ExceptionDay> searchExceptionDays(ExceptionDayFilterRequestDto exceptionDayFilterRequestDto) {
        Specification<ExceptionDay> spec = ExceptionDaySpecification.isBetween(exceptionDayFilterRequestDto);
        List<ExceptionDay> exceptionDays = exceptionDayRepository.findAll(spec);
        return exceptionDays;
    }

    public ExceptionDay getExceptionDayById(Long id) {
        ExceptionDay exceptionDay = exceptionDayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exception not found with id: " + id));
        return exceptionDay;
    }

    private ExceptionDay saveExceptionDay(ExceptionDay exceptionDay) {
        if (exceptionDay.getId() == null) {
            exceptionDay.setCreatedAt(LocalDateTime.now());
        }
        return exceptionDayRepository.save(exceptionDay);
    }
}
