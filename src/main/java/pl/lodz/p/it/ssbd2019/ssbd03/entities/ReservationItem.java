package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca relację wiele do wielu pomiedzy rezerwacją a przedmiotami.
 */
@Entity
@Table(name = "reservation_items", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ReservationItemId.class)
@NamedQueries(value = {
        @NamedQuery(
                name = "ReservationItem.getItemsForReservation",
                query = "select i from ReservationItem i where i.reservation.id = :reservationId"
        ),
        @NamedQuery(
                name = "ReservationItem.getItemsFromReservationInTimeFrame",
                query = "SELECT i FROM ReservationItem i WHERE i.reservation.id IN (" +
                            "SELECT DISTINCT r.id " +
                            "FROM Reservation r " +
                            "WHERE r.active = true and " +
                            "(r.startDate < :startTime and :startTime < r.endDate) or " +
                            "(r.startDate < :endTime and :endTime < r.endDate) or " +
                            "(:startTime < r.startDate and r.endDate < :endTime)" +
                        ")"
        )
})
public class ReservationItem {
    @Id
    @ManyToOne
    @JoinColumn(name = "reservation_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservation_items__reservation", value = ConstraintMode.CONSTRAINT))
    private Reservation reservation;

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservation_items__item", value = ConstraintMode.CONSTRAINT))
    private Item item;

    @Min(1)
    @NotNull
    @Column(name = "count", nullable = false)
    @ToString.Exclude
    private int count;

    @Version
    @NotNull
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
