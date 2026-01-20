package com.projeto.clinicaapi.Specification;

import com.projeto.clinicaapi.Dto.ExceptionDayDto.ExceptionDayFilterRequestDto;
import com.projeto.clinicaapi.Model.ScheduleModel.ExceptionDay;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class ExceptionDaySpecification {
    private static LocalDateTime extractStartDate(String startDate) {
        LocalDateTime start;
        if (startDate.length() == 4) {
            int year = Integer.parseInt(startDate);
            start = LocalDateTime.of(year, 1, 1, 0, 0);
        }
        else if (startDate.length() == 7) {
            YearMonth ym = YearMonth.parse(startDate);
            start = ym.atDay(1).atStartOfDay();
        }
        else if (startDate.length() == 10) {
            LocalDate ld = LocalDate.parse(startDate);
            start = ld.atStartOfDay();
        }
        else {
            start = LocalDateTime.parse(startDate);
        }
        return start;
    }
    private static LocalDateTime extractEndDate(String endDate) {
        LocalDateTime end;
        if (endDate.length() == 4) {
            int year = Integer.parseInt(endDate);
            end = LocalDateTime.of(year,12,31,23,59);
        }
        else if (endDate.length() == 7) {
            YearMonth ym = YearMonth.parse(endDate);
            end = ym.atEndOfMonth().atTime(LocalTime.MAX);
        }
        else if (endDate.length() == 10) {
            LocalDate ld = LocalDate.parse(endDate);
            end = ld.atTime(LocalTime.MAX);
        }
        else {
            end = LocalDateTime.parse(endDate);
        }
        return end;
    }
    public static Specification<ExceptionDay> isBetween(ExceptionDayFilterRequestDto exceptionFilterRequest){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exceptionFilterRequest.getStartTime() != null && !exceptionFilterRequest.getStartTime().isBlank()) {
                LocalDateTime start = extractStartDate(exceptionFilterRequest.getStartTime());
                predicates.add(cb.greaterThanOrEqualTo(root.get("endTime"), start));
            }
            if (exceptionFilterRequest.getEndTime() != null && !exceptionFilterRequest.getEndTime().isBlank()) {
                LocalDateTime end = extractEndDate(exceptionFilterRequest.getEndTime());
                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
