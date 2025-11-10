package com.projeto.maedopedro.Controller;

import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentRequestDto;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentResponseDto;
import com.projeto.maedopedro.Model.AppointmentModel.Appointment;
import com.projeto.maedopedro.Model.AppointmentModel.Status;
import com.projeto.maedopedro.Model.LolyaltUsersModel.Gender;
import com.projeto.maedopedro.Service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody AppointmentRequestDto appointment) {
        AppointmentResponseDto createdAppointment = appointmentService.createAppointment(appointment);
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
    public ResponseEntity<List<AppointmentResponseDto>> searchAppointment(
            @RequestParam (required = false) Status status,
            @RequestParam (required = false) LocalDateTime appointmentDate,
            @RequestParam (required = false) String firstName,
            @RequestParam (required = false) String lastName,
            @RequestParam (required = false) Gender gender,
            @RequestParam (required = false) String phoneNumber,
            @RequestParam (required = false) String cpf
            ){
        List<AppointmentResponseDto> appointments = appointmentService.searchAppointment(status, appointmentDate, firstName,
                lastName, gender, phoneNumber, cpf);
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointmentDto = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentDto);
    }


}
