package com.projeto.maedopedro.Specification;

import com.projeto.maedopedro.Model.AppointmentModel.Appointment;
import com.projeto.maedopedro.Model.AppointmentModel.Status;
import com.projeto.maedopedro.Model.LolyaltUsersModel.Gender;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AppointmentSpecification {
    public static Specification<Appointment> hasStatus(Status status) {
        return (root, query, cb) -> {
            return cb.equal(root.get("status"), status);
        };
    }
    public static Specification<Appointment> hasAppointmentDate(LocalDateTime dateAppointment) {
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("appointmentDate"), dateAppointment);
        };
    }
    public static Specification<Appointment> hasUserFirstName(String firstName) {
        return(root, query, criteriaBuilder) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return criteriaBuilder.like(userJoin.get("firstName"), "%"+firstName+"%");
        };
    }
    public static Specification<Appointment> hasUserLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return criteriaBuilder.like(userJoin.get("lastName"), "%"+lastName+"%");
        };
    }
    public static Specification<Appointment> hasGender(Gender gender) {
        return (root, query, criteriaBuilder) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return criteriaBuilder.equal(userJoin.get("gender"), gender);
        };
    }
    public static Specification<Appointment> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return criteriaBuilder.equal(userJoin.get("phoneNumber"), phoneNumber);
        };
    }
    public static Specification<Appointment> hasCpf(String cpf) {
        return (root, query, criteriaBuilder) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return criteriaBuilder.equal(userJoin.get("cpf"), cpf);
        };
    }
}
