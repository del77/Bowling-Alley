package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "service_requests", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "content", nullable = false, length = 256)
    private String content;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;
}
