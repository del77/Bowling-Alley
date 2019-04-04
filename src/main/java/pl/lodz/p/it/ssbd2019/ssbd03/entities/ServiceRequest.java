package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Entity
@Table(name = "service_requests", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    @Id
    @SequenceGenerator(name = "ServiceRequestSeqGen",
            sequenceName = "ServiceRequestSequence",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ServiceRequestSeqGen")
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private int id;

    @Column(name = "content", nullable = false, length = 256)
    private String content;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "date", nullable = false, updatable = false)
    private Timestamp date;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;
}
