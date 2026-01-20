package com.projeto.clinicaapi.Specification;

import com.projeto.clinicaapi.Model.AppointmentModel.Appointment;
import com.projeto.clinicaapi.Model.AppointmentModel.Status;
import com.projeto.clinicaapi.Model.LolyaltUsersModel.Gender;
import com.projeto.clinicaapi.Model.LolyaltUsersModel.LoyaltyUser;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentSpecification {
    public static Specification<Appointment> hasStatus(Status status) {
        return (root, query, cb) -> {
            return cb.equal(root.get("status"), status);
        };
    }
    public static Specification<Appointment> hasAppointmentDate(LocalDateTime dateAppointment) {
        return(root, query, cb) ->
            dateAppointment == null ? null : cb.equal(root.get("appointmentDate"), dateAppointment);
    }
    public static Specification<Appointment> hasUserFirstName(String firstName) {
        return(root, query, cb) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return (firstName == null || firstName.isBlank()) ? null : cb.like(userJoin.get("firstName"), "%"+firstName+"%");
        };
    }
    public static Specification<Appointment> hasUserLastName(String lastName) {
        return (root, query, cb) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return (lastName == null || lastName.isBlank()) ? null : cb.like(userJoin.get("lastName"), "%"+lastName+"%");
        };
    }
    public static Specification<Appointment> hasGender(Gender gender) {
        return (root, query, cb) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return gender == null ? null : cb.equal(userJoin.get("gender"), gender);
        };
    }
    public static Specification<Appointment> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return (phoneNumber == null || phoneNumber.isBlank()) ? null : cb.equal(userJoin.get("phoneNumber"), phoneNumber);
        };
    }
    public static Specification<Appointment> hasCpf(String cpf) {
        return (root, query, cb) -> {
            Join<Appointment, LoyaltyUser> userJoin = root.join("loyaltyUser");
            return (cpf == null || cpf.isBlank()) ? null : cb.equal(userJoin.get("cpf"), cpf);
        };
    }
}
