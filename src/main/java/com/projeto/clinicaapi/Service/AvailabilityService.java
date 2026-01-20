package com.projeto.clinicaapi.Service;

import com.projeto.clinicaapi.Dto.OfficeHourDto.OfficeHourDayResponseDto;
import com.projeto.clinicaapi.Dto.OfficeHourDto.OfficeHourMonthResponseDto;
import com.projeto.clinicaapi.Model.ScheduleModel.OfficeHour;
import com.projeto.clinicaapi.Repository.AppointmentRepository;
import com.projeto.clinicaapi.Repository.ExceptionDayRepository;
import com.projeto.clinicaapi.Repository.OfficeHourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.sql.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final OfficeHourRepository officeHourRepository;
    private final ExceptionDayRepository exceptionDayRepository;
    private final AppointmentRepository appointmentRepository;

    public List<OfficeHourMonthResponseDto> getMonthAvailability(int year, int month, Integer maxAppointments) {
        int maxApp = (maxAppointments != null) ? maxAppointments : 8;

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Map<DayOfWeek, OfficeHour> officeHoursMap = officeHourRepository.findAll()
                .stream()
                .collect(Collectors.toMap(OfficeHour::getDayOfWeek, Function.identity()));


        Set<LocalDate> blockedDaysList = exceptionDayRepository.findOverlapingExceptionsDays(month, officeHoursMap.get(DayOfWeek.MONDAY).getStartTime(),officeHoursMap.get(DayOfWeek.MONDAY).getEndTime())
                .stream().map(Date::toLocalDate).collect(Collectors.toSet());

        List<Object[]> rawCounts = appointmentRepository.countAppointmentsByPeriod(startDate, endDate);
        Map<LocalDate, Long> appointmentsCountMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> ((Date) row[0]).toLocalDate(),
                        row -> (Long) row[1],
                        Long::sum
                ));

        List<OfficeHourMonthResponseDto> dto = new ArrayList<>();

            for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
                long count = appointmentsCountMap.getOrDefault(day, 0L);

                if (!officeHoursMap.get(day.getDayOfWeek()).isOpen()) {
                    dto.add(new OfficeHourMonthResponseDto(day, "CLOSED"));
                }else if (blockedDaysList.contains(day)) {
                    dto.add(new OfficeHourMonthResponseDto(day, "OCCUPIED"));
                }else if (count >= maxApp) {
                    dto.add(new OfficeHourMonthResponseDto(day, "FULL"));
                }else {
                    dto.add(new OfficeHourMonthResponseDto(day, "OPEN"));
                }
            }
            return dto;
        }

        public List<OfficeHourDayResponseDto> getDayAvailability(LocalDate date) {
            List<LocalTime> allOpenSlots = getAllOpenSlots(date);
            List<OfficeHourDayResponseDto> dto = new ArrayList<>();
            for (LocalTime allOpenSlot : allOpenSlots) {
                dto.add(new OfficeHourDayResponseDto(date, allOpenSlot));
            }
            return dto;
        }
        public List<LocalTime> getAllOpenSlots(LocalDate date) {
            OfficeHour officeHour = officeHourRepository.getOfficeHourByDayOfWeek(date.getDayOfWeek());
            LocalTime startTime = officeHour.getStartTime();
            LocalTime endTime = officeHour.getEndTime();
            List<LocalTime> allNonExceptionSlots = getAllNonExceptionSlots(date, startTime, endTime);
            List<Time> allUsedAppointmentSlots = appointmentRepository.getAllByAppointmentHour(LocalDateTime.of(date,startTime)
                    ,LocalDateTime.of(date,endTime));
            Set<LocalTime> allUsedAppointmentSlotsRefactored = allUsedAppointmentSlots.stream().map(Time::toLocalTime).collect(Collectors.toSet());
            allNonExceptionSlots.removeAll(allUsedAppointmentSlotsRefactored);
            List<LocalTime> allOpenSlots = allNonExceptionSlots;
            return allOpenSlots;
        }
        public List<LocalTime> getAllNonExceptionSlots(LocalDate date, LocalTime startTime, LocalTime endTime) {
            List<LocalTime> allOpenSlots = new ArrayList<>();
            List<Object[]> rawList = exceptionDayRepository.findMinAndMaxExceptionDayHours(date.atStartOfDay(),date.atTime(LocalTime.MAX));
            Map<String, LocalTime> hoursMap = rawList.stream()
                    .filter(row -> row[0] != null && row[1] != null)
                    .collect(Collectors.toMap(
                            row -> (String) row[1],
                            row -> ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalTime()
                    ));
            long nonNullValues = hoursMap.values().stream().filter(Objects::nonNull).count();
            LocalTime minValue;
            LocalTime maxValue;
            if (nonNullValues == 2) {
                minValue = hoursMap.values().stream().min(Comparator.naturalOrder()).orElse(null);
                maxValue = hoursMap.values().stream().max(Comparator.naturalOrder()).orElse(null);
                for (LocalTime startHour = startTime; startHour.isBefore(endTime); startHour = startHour.plusMinutes(40)) {
                    if (startHour.plusMinutes(40).isBefore(minValue) || startHour.isAfter(maxValue)) {
                        allOpenSlots.add(startHour);
                    }
                }
            }
            if (nonNullValues == 1) {
                for (LocalTime startHour = startTime; startHour.isBefore(endTime); startHour = startHour.plusMinutes(40)) {
                        if(hoursMap.containsKey("start_time")) {
                            if(startHour.plusMinutes(40).isBefore(hoursMap.get("start_time"))) {
                                allOpenSlots.add(startHour);
                            }
                        }
                        if(hoursMap.containsKey("end_time")) {
                            if(startHour.isAfter(hoursMap.get("end_time"))) {
                                allOpenSlots.add(startHour);
                            }
                        }
                }
            } else if (nonNullValues == 0) {
                for (LocalTime startHour = startTime; startHour.isBefore(endTime); startHour = startHour.plusMinutes(40)) {
                    allOpenSlots.add(startHour);
                }
            }
            return allOpenSlots;
        }
    }