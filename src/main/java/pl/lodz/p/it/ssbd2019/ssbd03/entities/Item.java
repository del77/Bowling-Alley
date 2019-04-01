package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "size", nullable = false)
    private int size;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "version", nullable = false)
    private int version;

    @OneToMany(mappedBy="item")
    private List<ReservationItem> reservationItems;
}
