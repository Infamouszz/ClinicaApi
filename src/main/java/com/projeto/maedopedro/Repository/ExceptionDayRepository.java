package com.projeto.maedopedro.Repository;

import com.projeto.maedopedro.Model.ScheduleModel.ExceptionDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExceptionDayRepository extends JpaRepository<ExceptionDay, Integer>, JpaSpecificationExecutor<ExceptionDay> {
    @Query(value = """
        WITH RECURSIVE DayExploder AS (
            SELECT\s
                start_time,
                end_time,
                DATE(start_time) as data_analisada
            FROM exception_day
            WHERE :month between month(start_time) and month(end_time)
            UNION ALL
            SELECT\s
                start_time,
                end_time,
                DATE_ADD(data_analisada, INTERVAL 1 DAY)
            FROM DayExploder
            WHERE DATE_ADD(data_analisada, INTERVAL 1 DAY) <= DATE(end_time)
        )
        SELECT\s
            date(data_analisada) AS dia_inteiramente_fechado
        FROM DayExploder
        WHERE
            start_time <= TIMESTAMP(data_analisada, :startInterval)
            AND
            end_time >= TIMESTAMP(data_analisada, :endInterval)
        ORDER BY data_analisada;
    """, nativeQuery = true)
    List<Date> findOverlapingExceptionsDays(@Param("month")long month, @Param("startInterval") LocalTime startInterval, @Param("endInterval") LocalTime endInterval);

    @Query(value = """
        (
            SELECT\s
                data_ponto,\s
                origem
            FROM (
                SELECT start_time AS data_ponto, 'start_time' AS origem FROM exception_day\s
                WHERE start_time BETWEEN :startOfDay AND :endOfDay
                UNION ALL
                SELECT end_time AS data_ponto, 'end_time' AS origem FROM exception_day\s
                WHERE end_time BETWEEN :startOfDay AND :endOfDay
            ) as busca_min
            ORDER BY data_ponto ASC
            LIMIT 1
        )
        UNION ALL
        (
            SELECT\s
                data_ponto,\s
                origem
            FROM (
                SELECT start_time AS data_ponto, 'start_time' AS origem FROM exception_day\s
                WHERE start_time BETWEEN :startOfDay AND :endOfDay
                UNION ALL
                SELECT end_time AS data_ponto, 'end_time' AS origem FROM exception_day\s
                WHERE end_time BETWEEN :startOfDay AND :endOfDay
            ) as busca_max
            ORDER BY data_ponto DESC
            LIMIT 1
        )ORDER BY data_ponto ASC;
""", nativeQuery = true)
    List<Object[]> findMinAndMaxExceptionDayHours(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    Optional<ExceptionDay> findById(Long id);
}
