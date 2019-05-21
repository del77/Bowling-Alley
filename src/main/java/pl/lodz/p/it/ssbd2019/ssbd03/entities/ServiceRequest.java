package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    
    @NotNull
    @NotEmpty
    @Size(max = 256)
    @Column(name = "content", nullable = false, length = 256)
    @ToString.Exclude
    private String content;

    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
    
    @NotNull
    @Column(name = "date", nullable = false, updatable = false)
    @ToString.Exclude
    private Timestamp date;
    
    @NotNull
    @Column(name = "resolved", nullable = false)
    @ToString.Exclude
    private boolean resolved;
    
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade=CascadeType.REFRESH)
    @ToString.Exclude
    private UserAccount userAccount;
    
    @NotNull
    @JoinColumn(name = "alley_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @ToString.Exclude
    private Alley alley;
}
