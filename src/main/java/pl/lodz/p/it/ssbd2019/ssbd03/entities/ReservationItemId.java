package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * Klasa reprezentująca identyfikator dla relacj wiele do wielu pomiedzy kontem a poziomem dostępu.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationItemId implements Serializable {
    private Reservation reservation;
    private Item item;
}
