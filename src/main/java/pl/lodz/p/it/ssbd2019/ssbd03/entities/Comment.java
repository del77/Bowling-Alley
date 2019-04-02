package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Entity
@Table(name = "comments", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @SequenceGenerator(name = "CommentSeqGen", sequenceName = "CommentSequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CommentSeqGen")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private int id;

    @ManyToOne
    @JoinColumn(name = "reservation_id",
            updatable = false,
            unique = true,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__comment__reservation", value = ConstraintMode.CONSTRAINT))
    private Reservation reservation;

    @NotEmpty
    @Column(name = "content", nullable = false, length = 256)
    private String content;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "date", nullable = false)
    private Timestamp date;
}
