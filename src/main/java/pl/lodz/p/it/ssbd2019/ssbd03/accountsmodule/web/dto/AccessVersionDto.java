package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;

/**
 * Klasa przechowująca dane dotyczące poziomów dostępu użytkownika.
 */
@Data
public class AccessVersionDto {
    String name;
    Long version;
    boolean selected;
    public AccessVersionDto(String name, Long version, boolean selected) {
        this.name = name;
        this.version = version;
        this.selected = selected;
    }
}
