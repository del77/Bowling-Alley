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
    @ManyToOne
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
