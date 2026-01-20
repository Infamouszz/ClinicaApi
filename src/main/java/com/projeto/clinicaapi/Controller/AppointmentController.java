package com.projeto.clinicaapi.Controller;

import com.projeto.clinicaapi.Dto.AppointmentDto.AppointmentRequestDto;
import com.projeto.clinicaapi.Dto.AppointmentDto.AppointmentResponseDto;
import com.projeto.clinicaapi.Dto.AppointmentDto.AppointmentSearchRequestDto;
import com.projeto.clinicaapi.Dto.OfficeHourDto.OfficeHourDayResponseDto;
import com.projeto.clinicaapi.Dto.OfficeHourDto.OfficeHourMonthResponseDto;
import com.projeto.clinicaapi.Service.AppointmentService;
import com.projeto.clinicaapi.Service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AvailabilityService availabilityService;

    @PostMapping("/create/{cpf}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppointmentResponseDto> createAppointment(@PathVariable String cpf, @RequestBody AppointmentRequestDto appointment) {
        AppointmentResponseDto createdAppointment = appointmentService.createAppointment(cpf,appointment);
        return ResponseEntity.ok(createdAppointment);
    }

    //CONFIRMAR, CANCELAR, COMPLETAR E REMARCAR CONSULTA
    @PatchMapping("/confirm/{id}")
    public ResponseEntity<AppointmentResponseDto> confirmAppointment(@PathVariable Long id) {
        AppointmentResponseDto confirmedAppointment = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(confirmedAppointment);
    }
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(@PathVariable Long id) {
        AppointmentResponseDto canceledAppointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(canceledAppointment);
    }
    @PatchMapping("/complete/{id}")
    public ResponseEntity<AppointmentResponseDto> completeAppointment(@PathVariable Long id) {
        AppointmentResponseDto completedAppointment = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(completedAppointment);
    }
    @PatchMapping("/reschedule/{id}")
    public ResponseEntity<AppointmentResponseDto> rescheduleAppointment(@PathVariable Long id, @RequestBody LocalDateTime rescheduleDate) {
        AppointmentResponseDto rescheduledAppointment = appointmentService.rescheduleAppointment(id, rescheduleDate);
        return ResponseEntity.ok(rescheduledAppointment);
    }
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> searchAppointment(AppointmentSearchRequestDto appointmentSearchRequestDto){
        List<AppointmentResponseDto> appointments = appointmentService.searchAppointment(appointmentSearchRequestDto);
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointmentDto = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentDto);
    }
    @GetMapping("/availability")
    public ResponseEntity<List<OfficeHourMonthResponseDto>> getMonthAvailableDates(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam (required = false) Integer maxAppointments
            ){
        List<OfficeHourMonthResponseDto> officeDto = availabilityService.getMonthAvailability(year,month,maxAppointments);
        return ResponseEntity.ok(officeDto);
    }
    @GetMapping("/availability/slots/{date}")
    public ResponseEntity<List<OfficeHourDayResponseDto>> getHourAvailabilityByDay(@PathVariable LocalDate date){
        List<OfficeHourDayResponseDto> officeHourDto = availabilityService.getDayAvailability(date);
        return ResponseEntity.ok(officeHourDto);
    }



}
