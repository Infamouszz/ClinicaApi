package com.projeto.maedopedro.Service;
import com.projeto.maedopedro.Dto.ExceptionDayDto.ExceptionDayCreateRequestDto;
import com.projeto.maedopedro.Dto.ExceptionDayDto.ExceptionDayFilterRequestDto;
import com.projeto.maedopedro.Dto.ExceptionDayDto.ExceptionDayUpdateRequestDto;
import com.projeto.maedopedro.ExceptionHandler.Exception.BusinessLogicException;
import com.projeto.maedopedro.ExceptionHandler.Exception.InvalidEntryException;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceNotFoundException;
import com.projeto.maedopedro.Mapper.ExceptionDayMapper;
import com.projeto.maedopedro.Model.ScheduleModel.ExceptionDay;
import com.projeto.maedopedro.Repository.ExceptionDayRepository;
import com.projeto.maedopedro.Specification.ExceptionDaySpecification;
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
