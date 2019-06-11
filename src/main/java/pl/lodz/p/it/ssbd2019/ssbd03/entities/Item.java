package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca przedmioty.
 */
@Entity
@Table(name = "items", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries(
        value = {
                @NamedQuery(name = "Item.findByItemType", query = "SELECT i FROM Item i JOIN i.itemType it WHERE it.name = :itemType order by i.size"),
        }
)
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Min(0)
    @NotNull
    @Column(name = "size", nullable = false)
    @ToString.Exclude
    private int size;

    @Min(0)
    @NotNull
    @Column(name = "count", nullable = false)
    @ToString.Exclude
    private int count;

    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "item_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__item__item_type", value = ConstraintMode.CONSTRAINT))
    @ToString.Exclude
    private ItemType itemType;
}
