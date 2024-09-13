package com.design_pattern.sidecar_pattern.transaction.service;

import com.design_pattern.sidecar_pattern.transaction.dto.Reservation;
import com.design_pattern.sidecar_pattern.transaction.mapper.ReservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public void processReservation(Reservation reservation) {
        // 전달된 예약 정보를 저장한다.
        reservation.setStatus("PENDING");
        reservationMapper.insertReservation(reservation);

        // 예약 정보를 스텝별로 저장 후 예약 상태를 업데이트한다.
        step1(reservation);
        step2(reservation);
        step3(reservation);
        step4(reservation);
        step5(reservation);
        step6(reservation);
    }

    @Transactional
    public void compensateReservation(String reservationId) {
        reservationMapper.updateReservationStatus(reservationId, "CANCELLED");
        logger.info("Reservation status updated to CANCELLED for reservation: {}", reservationId);
    }

    private void step1(Reservation reservation) {
        // Step 1 logic
        saveStepStatus(reservation.getId(), "STEP1", "COMPLETED");
        reservation.setStatus("STEP1_COMPLETED");
        reservationMapper.updateReservationStatus(reservation.getId(), reservation.getStatus());
        logger.info("Reservation status updated to STEP1_COMPLETED for reservation: {}", reservation.getId());
    }

    private void step2(Reservation reservation) {
        // Step 2 logic
        saveStepStatus(reservation.getId(), "STEP2", "COMPLETED");
        reservation.setStatus("STEP2_COMPLETED");
        reservationMapper.updateReservationStatus(reservation.getId(), reservation.getStatus());
        logger.info("Reservation status updated to STEP2_COMPLETED for reservation: {}", reservation.getId());
    }

    private void step3(Reservation reservation) {
        // Step 3 logic
        saveStepStatus(reservation.getId(), "STEP3", "COMPLETED");
        reservation.setStatus("STEP3_COMPLETED");
        reservationMapper.updateReservationStatus(reservation.getId(), reservation.getStatus());
        logger.info("Reservation status updated to STEP3_COMPLETED for reservation: {}", reservation.getId());
    }

    private void step4(Reservation reservation) {
        // Step 4 logic
        saveStepStatus(reservation.getId(), "STEP4", "COMPLETED");
        reservation.setStatus("STEP4_COMPLETED");
        reservationMapper.updateReservationStatus(reservation.getId(), reservation.getStatus());
        logger.info("Reservation status updated to STEP4_COMPLETED for reservation: {}", reservation.getId());
    }

    private void step5(Reservation reservation) {
        // Step 5 logic
        saveStepStatus(reservation.getId(), "STEP5", "COMPLETED");
        reservation.setStatus("STEP5_COMPLETED");
        reservationMapper.updateReservationStatus(reservation.getId(), reservation.getStatus());
        logger.info("Reservation status updated to STEP5_COMPLETED for reservation: {}", reservation.getId());
    }

    private void step6(Reservation reservation) {
        // Step 6 logic
        saveStepStatus(reservation.getId(), "STEP6", "COMPLETED");
        reservation.setStatus("COMPLETED");
        reservationMapper.updateReservationStatus(reservation.getId(), reservation.getStatus());
        logger.info("Reservation status updated to COMPLETED for reservation: {}", reservation.getId());
    }

    private void saveStepStatus(String reservationId, String stepName, String status) {
        reservationMapper.insertStepStatus(reservationId, stepName, status);
        logger.info("Step status saved: reservationId={}, stepName={}, status={}", reservationId, stepName, status);
    }
}