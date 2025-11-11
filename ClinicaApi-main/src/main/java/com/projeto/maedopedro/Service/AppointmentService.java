package com.projeto.maedopedro.Service;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentRequestDto;
import com.projeto.maedopedro.Dto.AppointmentDto.AppointmentResponseDto;
import com.projeto.maedopedro.Dto.LoyaltUserDto.LoyaltyUserAppointmentDto;
import com.projeto.maedopedro.Model.AppointmentModel.Appointment;
import com.projeto.maedopedro.Model.AppointmentModel.Status;
import com.projeto.maedopedro.Model.LolyaltUsersModel.Gender;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import com.projeto.maedopedro.Repository.AppointmentRepository;
import com.projeto.maedopedro.Repository.LoyaltyUserRepository;
import com.projeto.maedopedro.Specification.AppointmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final LoyaltyUserRepository loyaltyUserRepository;

    //CREATE
    @Transactional
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        LoyaltyUser loyaltyUser = loyaltyUserRepository.findByCpf(appointmentRequestDto.getCpf())
                .orElseThrow(() -> new RuntimeException("LoyaltyUser not found"));
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
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointmentToUpdated.setStatus(Status.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdated);
        return convertToResponseDto(savedAppointment);
    }
    @Transactional
    public AppointmentResponseDto cancelAppointment(Long appointmentId) {
        Appointment appointmentToUpdated = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointmentToUpdated.setStatus(Status.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdated);
        return convertToResponseDto(savedAppointment);
    }
    @Transactional
    public AppointmentResponseDto completeAppointment(Long appointmentId) {
        Appointment appointmentToUpdated = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointmentToUpdated.setStatus(Status.COMPLETED);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdated);
        return convertToResponseDto(savedAppointment);
    }
    @Transactional
    public AppointmentResponseDto rescheduleAppointment(Long appointmentId, LocalDateTime rescheduleDate) {
        Appointment appointmentToUpdate = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointmentToUpdate.setAppointmentDate(rescheduleDate);
        Appointment savedAppointment = appointmentRepository.save(appointmentToUpdate);
        return convertToResponseDto(savedAppointment);
    }

    //GETTERS
    public List<AppointmentResponseDto> searchAppointment(Status status, LocalDateTime dateAppointment,
                                                          String firsName, String lastName, Gender gender,
                                                          String phoneNumber, String cpf) {
        Specification<Appointment> spec = Specification.unrestricted();
        if (status != null) {
            switch (status) {
                case CONFIRMED -> spec = spec.and(AppointmentSpecification.hasStatus(Status.CONFIRMED));
                case CANCELLED -> spec = spec.and(AppointmentSpecification.hasStatus(Status.CANCELLED));
                case COMPLETED -> spec = spec.and(AppointmentSpecification.hasStatus(Status.COMPLETED));
                case PENDING -> spec = spec.and(AppointmentSpecification.hasStatus(Status.PENDING));
            }
        }
        if (dateAppointment != null) {
            spec = spec.and(AppointmentSpecification.hasAppointmentDate(dateAppointment));
        }
        if (StringUtils.hasText(firsName)){
            spec = spec.and(AppointmentSpecification.hasUserFirstName(firsName));
        }
        if (StringUtils.hasText(lastName)){
            spec = spec.and(AppointmentSpecification.hasUserLastName(lastName));
        }
        if (gender != null) {
            spec = spec.and(AppointmentSpecification.hasGender(gender));
        }
        if (StringUtils.hasText(phoneNumber)){
            spec = spec.and(AppointmentSpecification.hasPhoneNumber(phoneNumber));
        }
        if (StringUtils.hasText(cpf)){
            spec = spec.and(AppointmentSpecification.hasCpf(cpf));
        }
        List<Appointment> appointments = appointmentRepository.findAll(spec);
        return appointments.stream().map(this::convertToResponseDto).toList();
    }
    public AppointmentResponseDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
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
        LoyaltyUserAppointmentDto userDto = new LoyaltyUserAppointmentDto(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getMotherName(),
                user.getFatherName()
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
