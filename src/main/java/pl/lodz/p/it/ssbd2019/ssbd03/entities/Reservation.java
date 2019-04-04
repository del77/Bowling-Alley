package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "reservations", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @SequenceGenerator(name = "ReservationSeqGen",
            sequenceName = "ReservationSequence",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReservationSeqGen")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private int id;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Min(1)
    @Column(name = "players_count", nullable = false)
    private int playersCount;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;
}
