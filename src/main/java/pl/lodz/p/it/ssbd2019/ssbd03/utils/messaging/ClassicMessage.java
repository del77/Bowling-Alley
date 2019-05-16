package pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging;

import lombok.*;

/**
 * Klasa, która reprezentuje w aplikacji wiadomość.
 */
@Builder
@Data
public class ClassicMessage {
    String from;
    String to;
    String subject;
    String body;
}
