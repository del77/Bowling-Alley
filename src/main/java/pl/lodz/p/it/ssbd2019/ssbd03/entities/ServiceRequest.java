package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
/**
 * Klasa reprezentująca zapytania serwisowe.
 */
@Entity
@Table(name = "service_requests", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "content", nullable = false, length = 256)
    private String content;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;

    @Column(name = "date", nullable = false, updatable = false)
    private Timestamp date;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade=CascadeType.REFRESH)
    private UserAccount userAccount;

    @JoinColumn(name = "alley_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Alley alley;
}
