package pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormData {
    private Object data;
    private List<String> errors;
    private List<String> infos;
}
