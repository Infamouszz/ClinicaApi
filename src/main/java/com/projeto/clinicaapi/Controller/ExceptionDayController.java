package com.projeto.clinicaapi.Controller;

import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayCreateRequestDto;
import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayFilterRequestDto;
import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayUpdateRequestDto;
import com.projeto.clinicaapi.Model.ScheduleModel.ExceptionDay;
import com.projeto.clinicaapi.Service.ExceptionDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exception-day")
@RequiredArgsConstructor
public class ExceptionDayController {
    private final ExceptionDayService exceptionDayService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExceptionDay> createExceptionDay(@RequestBody ExceptionDayCreateRequestDto exceptionDayCreateRequestDto) {
        ExceptionDay createdExceptionDay = exceptionDayService.createExceptionDays(exceptionDayCreateRequestDto);
        return ResponseEntity.ok(createdExceptionDay);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExceptionDay(@PathVariable Long id) {
        exceptionDayService.deleteExceptionDays(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExceptionDay> getExceptionDayById(@PathVariable Long id) {
        ExceptionDay exceptionDay = exceptionDayService.getExceptionDayById(id);
        return ResponseEntity.ok(exceptionDay);
    }

    @GetMapping
    public ResponseEntity<List<ExceptionDay>> searchExceptionDays(
            ExceptionDayFilterRequestDto exceptionDayFilterRequestDto) {
        List<ExceptionDay> exceptionDays = exceptionDayService.searchExceptionDays(exceptionDayFilterRequestDto);
        return ResponseEntity.ok(exceptionDays);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ExceptionDay> updateExceptionDay(@RequestBody ExceptionDayUpdateRequestDto exceptionDayUpdateRequest,
                                                           @PathVariable Long id) {
        ExceptionDay exceptionDay = exceptionDayService.updateExceptionDays(exceptionDayUpdateRequest,id);
        return ResponseEntity.ok(exceptionDay);
    }

}
