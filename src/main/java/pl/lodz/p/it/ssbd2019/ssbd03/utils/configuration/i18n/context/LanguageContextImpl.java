package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.annotations.KnownLocale;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa implemetnująca LanguageContext
 * Przechowuje stan języka dla sesji użytkownika.
 */
@SessionScoped
public class LanguageContextImpl implements LanguageContext, Serializable {

    @Inject
    @KnownLocale("EN")
    private LocaleConfig english;

    @Inject
    @KnownLocale("PL")
    private LocaleConfig polish;

    private LocaleConfig currentLocale;

    @Override
    public LocaleConfig getDefault() {
        return polish;
    }

    @Override
    public LocaleConfig getLocaleConfig(String name) {
        if (name.equalsIgnoreCase(polish.locale().getLanguage())) {
            return polish;
        } else {
            return english;
        }
    }

    @Override
    public List<LocaleConfig> getAllLocaleConfig() {
        return Arrays.asList(polish, english);
    }

    @Override
    public LocaleConfig getCurrent() {
        if (this.currentLocale == null) {
            this.currentLocale = getDefault();
        }
        return this.currentLocale;
    }

    /**
     * Ustawia konfigurację tylko jesli należy do dostępnych.
     * @param localeConfig konfiguracja do ustawienia.
     */
    @Override
    public void setCurrent(LocaleConfig localeConfig) {
        for (LocaleConfig config : getAllLocaleConfig()) {
            if (config.equals(localeConfig)) {
                this.currentLocale = localeConfig;
                break;
            }
        }
    }
}
