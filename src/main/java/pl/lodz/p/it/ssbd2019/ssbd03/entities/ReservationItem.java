package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "reservation_items", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ReservationItemId.class)
public class ReservationItem {
    @Id
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "version", nullable = false)
    private int version;
}
