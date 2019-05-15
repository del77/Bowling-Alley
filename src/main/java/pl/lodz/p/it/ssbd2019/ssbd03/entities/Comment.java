package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
/**
 * Klasa reprezentująca komentarze do rezerwacji.
 */
@Entity
@Table(name = "comments", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id",
            updatable = false,
            unique = true,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__comment__reservation", value = ConstraintMode.CONSTRAINT))
    private Reservation reservation;

    @NotEmpty
    @NotNull
    @Size(max = 256)
    @Column(name = "content", nullable = false, length = 256)
    private String content;
    
    @NotNull
    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
}
