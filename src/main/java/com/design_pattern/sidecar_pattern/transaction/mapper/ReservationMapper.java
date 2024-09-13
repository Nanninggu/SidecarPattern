package com.design_pattern.sidecar_pattern.transaction.mapper;

import com.design_pattern.sidecar_pattern.transaction.dto.Reservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {

    int insertReservation(Reservation reservation);

    void updateReservationStatus(String id, String status);

    int insertStepStatus(String reservationId, String stepName, String status);
}
