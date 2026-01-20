package com.projeto.maedopedro.Service;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentRequestDto;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentResponseDto;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentLoyaltyUserRequestDto;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentSearchRequestDto;
import com.projeto.maedopedro.ExceptionHandler.Exception.ResourceNotFoundException;
import com.projeto.maedopedro.Model.AppointmentModel.Appointment;
import com.projeto.maedopedro.Model.AppointmentModel.Status;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import com.projeto.maedopedro.Repository.AppointmentRepository;
import com.projeto.maedopedro.Repository.LoyaltyUserRepository;
import com.projeto.maedopedro.Specification.AppointmentSpecification;
import com.projeto.maedopedro.Validator.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final LoyaltyUserRepository loyaltyUserRepository;
    private final AppointmentValidator appointmentValidator;

    //CREATE
    @Transactional
    public AppointmentResponseDto createAppointment(String cpf, AppointmentRequestDto appointmentRequestDto) {
        appointmentValidator.validateAppointment(appointmentRequestDto.getAppointmentDate());
        LoyaltyUser loyaltyUser = loyaltyUserRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyUser not found with cpf: " + cpf));
        Appointment appointment = Appointment.builder()
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .observation(appointmentRequestDto.getObservation())
                .loyaltyUser(loyaltyUser).build();
        Appointment savedAppointment = saveAppointment(appointment);
        return convertToResponseDto(savedAppointment);
    }

    //CONFIRMAR, CANCELAR, COMPLETAR E REMARCAR CONSULTAS
    @Transactional
    public AppointmentResponseDto confirmAppointment(Long appointmentId) {
        Appointment appointmentToUpdated = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment no found with id: " + appointmentId));
        appointmentToUpdated.setStatus(Status.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdated);
        return convertToResponseDto(savedAppointment);
    }
    @Transactional
    public AppointmentResponseDto cancelAppointment(Long appointmentId) {
        Appointment appointmentToUpdated = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment no found with id: " + appointmentId));
        appointmentToUpdated.setStatus(Status.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdated);
        return convertToResponseDto(savedAppointment);
    }
    @Transactional
    public AppointmentResponseDto completeAppointment(Long appointmentId) {
        Appointment appointmentToUpdated = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment no found with id: " + appointmentId));
        appointmentToUpdated.setStatus(Status.COMPLETED);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdated);
        return convertToResponseDto(savedAppointment);
    }
    @Transactional
    public AppointmentResponseDto rescheduleAppointment(Long appointmentId, LocalDateTime rescheduleDate) {
        appointmentValidator.validateAppointment(rescheduleDate);
        Appointment appointmentToCancel = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment no found with id: " + appointmentId));
        LoyaltyUser loyaltyUser = appointmentToCancel.getLoyaltyUser();
        appointmentToCancel.setStatus(Status.RESCHEDULED);
        appointmentRepository.save(appointmentToCancel);
        Appointment rescheduledAppointment = Appointment.builder()
                .appointmentDate(appointmentToCancel.getAppointmentDate())
                .observation(appointmentToCancel.getObservation())
                .loyaltyUser(loyaltyUser).build();
        return convertToResponseDto(rescheduledAppointment);
    }

    //GETTERS
    public List<AppointmentResponseDto> searchAppointment(AppointmentSearchRequestDto appointmentSearchRequestDto) {
        Specification<Appointment> spec = Specification.allOf(
                AppointmentSpecification.hasAppointmentDate(appointmentSearchRequestDto.getDateAppointment()),
                AppointmentSpecification.hasUserFirstName(appointmentSearchRequestDto.getFirstName()),
                AppointmentSpecification.hasUserLastName(appointmentSearchRequestDto.getLastName()),
                AppointmentSpecification.hasGender(appointmentSearchRequestDto.getGender()),
                AppointmentSpecification.hasPhoneNumber(appointmentSearchRequestDto.getPhoneNumber()),
                AppointmentSpecification.hasCpf(appointmentSearchRequestDto.getCpf()));
        if (appointmentSearchRequestDto.getStatus() != null) {
            switch (appointmentSearchRequestDto.getStatus()) {
                case CONFIRMED -> spec = AppointmentSpecification.hasStatus(Status.CONFIRMED);
                case CANCELLED -> spec = AppointmentSpecification.hasStatus(Status.CANCELLED);
                case COMPLETED -> spec = AppointmentSpecification.hasStatus(Status.COMPLETED);
                case PENDING -> spec = AppointmentSpecification.hasStatus(Status.PENDING);
            }
        }
        List<Appointment> appointments = appointmentRepository.findAll(spec);
        return appointments.stream().map(this::convertToResponseDto).toList();
    }
    public AppointmentResponseDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment no found with id: " + appointmentId));
        return convertToResponseDto(appointment);
    }

    //MÉTODOS AUXILIARES
    private Appointment saveAppointment(Appointment newAppointment) {
        if (newAppointment.getAppointmentId() == null) {
            newAppointment.setCreatedAt(LocalDateTime.now());
            newAppointment.setStatus(Status.PENDING);
        }
        return appointmentRepository.save(newAppointment);
    }
    private AppointmentResponseDto convertToResponseDto(Appointment savedAppointment) {
        LoyaltyUser user = savedAppointment.getLoyaltyUser();
        AppointmentLoyaltyUserRequestDto userDto = new AppointmentLoyaltyUserRequestDto(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getMotherName(),
                user.getFatherName(),
                user.getGender()
        );
        return new AppointmentResponseDto(
                savedAppointment.getAppointmentId(),
                savedAppointment.getAppointmentDate(),
                savedAppointment.getStatus(),
                savedAppointment.getCreatedAt(),
                userDto,
                savedAppointment.getObservation()
        );
    }

}
