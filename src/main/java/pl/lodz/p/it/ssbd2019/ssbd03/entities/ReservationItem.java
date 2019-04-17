package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "item_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservation_items__item", value = ConstraintMode.CONSTRAINT))
    private Item item;

    @Min(0)
    @Column(name = "count", nullable = false)
    private int count;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
