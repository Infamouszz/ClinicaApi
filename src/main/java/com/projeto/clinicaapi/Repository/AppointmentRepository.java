package com.projeto.clinicaapi.Repository;

import com.projeto.clinicaapi.Model.AppointmentModel.Appointment;
import com.projeto.clinicaapi.Model.AppointmentModel.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    boolean existsAppointmentsByAppointmentDateAndStatusAndStatus(LocalDateTime appointmentDate, Status status, Status status1);

    @Query(value = "SELECT COUNT(*) FROM appointment a WHERE DATE(a.appointment_date) = :data AND a.status = 'PENDING' OR a.status = 'CONFIRMED'", nativeQuery = true )
    long countAppointmentsByAppointmentDateAndStatus_PendingAndStatus_Completed(@Param("data") LocalDate date);

    @Query (value = "SELECT TIME(appointment_date) FROM appointment WHERE appointment_date >= :startHour AND appointment_date <= :endHour", nativeQuery = true)
    List<Time> getAllByAppointmentHour(@Param("startHour")LocalDateTime startHour, @Param("endHour") LocalDateTime endHour);

    @Query("""
    SELECT DATE(a.appointmentDate), COUNT(a) 
    FROM Appointment a 
    WHERE DATE(a.appointmentDate) BETWEEN :start AND :end 
    AND (a.status = 'PENDING' OR a.status = 'COMPLETED') 
    GROUP BY (a.appointmentDate)
""")
    List<Object[]> countAppointmentsByPeriod(LocalDate start, LocalDate end);
}
