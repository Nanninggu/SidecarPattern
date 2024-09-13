package com.design_pattern.sidecar_pattern.transaction.controller;

import com.design_pattern.sidecar_pattern.transaction.dto.Reservation;
import com.design_pattern.sidecar_pattern.transaction.orchestrator.ReservationSagaOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationSagaOrchestrator reservationSagaOrchestrator;

    @Autowired
    public ReservationController(ReservationSagaOrchestrator reservationSagaOrchestrator) {
        this.reservationSagaOrchestrator = reservationSagaOrchestrator;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        try {
            reservationSagaOrchestrator.executeReservationSaga(reservation);
            return new ResponseEntity<>("Reservation processed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Reservation processing failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
