package com.projeto.clinicaapi.Model.AppointmentModel;
import com.projeto.clinicaapi.Model.LolyaltUsersModel.LoyaltyUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "loyalty_user_id")
    private LoyaltyUser loyaltyUser;

    private String observation;
}
