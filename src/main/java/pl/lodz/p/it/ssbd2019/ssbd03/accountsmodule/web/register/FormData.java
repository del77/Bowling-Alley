package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;

import java.util.List;

@AllArgsConstructor
@Data
class FormData {
    private BasicAccountDto basicAccountDto;
    private List<String> errors;
}
