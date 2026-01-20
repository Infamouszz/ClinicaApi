package com.projeto.maedopedro.Seed;

import com.projeto.maedopedro.Model.ScheduleModel.OfficeHour;
import com.projeto.maedopedro.Repository.OfficeHourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfficeHourSeed implements CommandLineRunner {

    private final OfficeHourRepository officeHourRepository;

    @Override
    public void run(String... args) throws Exception {
        if (officeHourRepository.count() == 0) {
            log.info("Fulling table...");

            OfficeHour monday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.MONDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(18, 0))
                    .isOpen(true)
                    .build();

            OfficeHour tuesday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.TUESDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(18, 0))
                    .isOpen(true)
                    .build();

            OfficeHour wednesday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.WEDNESDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(18, 0))
                    .isOpen(true)
                    .build();
            OfficeHour thursday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.THURSDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(18, 0))
                    .isOpen(true)
                    .build();
            OfficeHour friday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.FRIDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(18, 0))
                    .isOpen(true)
                    .build();
            OfficeHour saturday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.SATURDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(12, 0))
                    .isOpen(true)
                    .build();
            OfficeHour sunday = OfficeHour.builder()
                    .dayOfWeek(DayOfWeek.SUNDAY)
                    .startTime(LocalTime.of(0, 0))
                    .endTime(LocalTime.of(0, 0))
                    .isOpen(false)
                    .build();

            officeHourRepository.saveAll(List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday));

            log.info("Table successfully fulfilled!");
        } else {
            log.info("Table already fully fulfilled!.");
        }
    }
}
