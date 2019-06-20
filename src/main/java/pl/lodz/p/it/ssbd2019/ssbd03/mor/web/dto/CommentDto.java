package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Timestamp date;
    private boolean active;
}
