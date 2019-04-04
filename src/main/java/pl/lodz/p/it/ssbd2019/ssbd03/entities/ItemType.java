package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "item_types", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemType {
    @Id
    @SequenceGenerator(name = "ItemTypeSeqGen",
            sequenceName = "ItemTypeSequence",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemTypeSeqGen")
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private int id;

    @NotEmpty
    @Column(name = "name", nullable = false, length = 25, unique = true)
    private String name;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;
}
