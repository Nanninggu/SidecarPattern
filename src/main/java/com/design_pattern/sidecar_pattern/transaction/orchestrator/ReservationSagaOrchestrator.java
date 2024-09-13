package com.design_pattern.sidecar_pattern.transaction.orchestrator;

import com.design_pattern.sidecar_pattern.transaction.dto.Reservation;
import com.design_pattern.sidecar_pattern.transaction.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationSagaOrchestrator {
    private final ReservationService reservationService;

    @Autowired
    public ReservationSagaOrchestrator(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void executeReservationSaga(Reservation reservation) {
        try {
            reservationService.processReservation(reservation);
        } catch (Exception ex) {
            reservationService.compensateReservation(reservation.getId());
            throw ex; // throw로 예외를 다시 던지면, PaymentController에서 catch하여 처리할 수 있음.
        }
    }
}
