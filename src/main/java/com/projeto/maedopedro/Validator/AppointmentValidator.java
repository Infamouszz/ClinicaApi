package com.projeto.maedopedro.Validator;

import com.projeto.maedopedro.ExceptionHandler.Exception.BusinessLogicException;
import com.projeto.maedopedro.ExceptionHandler.Exception.InvalidEntryException;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceAlreadyExistsException;
import com.projeto.maedopedro.Model.AppointmentModel.Status;
import com.projeto.maedopedro.Repository.AppointmentRepository;
import com.projeto.maedopedro.Service.AvailabilityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class AppointmentValidator {
    private final AppointmentRepository appointmentRepository;
    private final AvailabilityService availabilityService;
    public void validateAppointment(LocalDateTime appointmentDate) {
        if(appointmentDate.isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException("Appointment date cannot be in the past");
        }
        if (appointmentRepository.existsAppointmentsByAppointmentDateAndStatusAndStatus(appointmentDate,Status.PENDING,Status.CONFIRMED)) {
            throw new ResourceAlreadyExistsException("Appointment already exists");
        }
        if (appointmentRepository.countAppointmentsByAppointmentDateAndStatus_PendingAndStatus_Completed(appointmentDate.toLocalDate()) >= 8) {
            throw new BusinessLogicException("Day already full");
        }
        List<LocalTime> allOpenSlots = availabilityService.getAllOpenSlots(appointmentDate.toLocalDate());
        if (!allOpenSlots.contains(appointmentDate.toLocalTime())) {
            throw new ResourceAlreadyExistsException("Slot not available");
        }
    }
}
