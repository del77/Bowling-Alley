package pl.lodz.p.it.ssbd2019.ssbd03.utils.breadcrumbs;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Klasa reprezenuje pojedyńczy segment lokalizacji użytkownika w aplikacji, tzw. "Okruszki chleba".
 */
@AllArgsConstructor
@Getter
public class Breadcrumb {
    String label;
    String href;
    boolean disabled;
}
