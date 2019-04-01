package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "items", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @SequenceGenerator(name = "ItemSeqGen", sequenceName = "ItemSequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemSeqGen")
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private int id;

    @Min(0)
    @Column(name = "size", nullable = false)
    private int size;

    @Min(0)
    @Column(name = "count", nullable = false)
    private int count;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;

    @ManyToOne
    @JoinColumn(name = "item_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__item__item_type", value = ConstraintMode.CONSTRAINT))
    private ItemType itemType;
}
