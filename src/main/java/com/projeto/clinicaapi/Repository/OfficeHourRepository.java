package com.projeto.clinicaapi.Repository;


import com.projeto.clinicaapi.Model.ScheduleModel.OfficeHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface OfficeHourRepository extends JpaRepository<OfficeHour, Long> {

    Optional<OfficeHour> findByDayOfWeek(DayOfWeek dayOfWeek);

    OfficeHour getOfficeHourByDayOfWeek(DayOfWeek dayOfWeek);
}
