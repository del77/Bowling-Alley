package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context;

import java.util.List;

/**
 * Klasa kontekstu jezyka.
 */
public interface LanguageContext {
    /**
     * @return konfiguracją dla domyślego Locale.
     */
    LocaleConfig getDefault();

    /**
     * @return zwraca czy obecne ustawienie językowe jest preferowane (zmiana manualna).
     */
    boolean isPreffered();

    /**
     * @param name nazwa jezyka.
     * @return konfigurację dla Locale języka o podanej nazwie.
     */
    LocaleConfig getLocaleConfig(String name);

    /**
     * @return wszystkei dostępne w kontekście konfiguracje Locale.
     */
    List<LocaleConfig> getAllLocaleConfig();

    /**
     * @return zwraca konfigurację dla obecnie wybranego języka.
     */
    LocaleConfig getCurrent();

    /**
     * Pozwala ustawić nową konfigurację jezyka.
     * @param localeConfig konfiguracja do ustawienia.
     */
    void setCurrent(LocaleConfig localeConfig);

    /**
     * Pozwala ustawić nową konfigurację jezyka, wraz z flagą preferencji dla niej.
     * @param localeConfig konfiguracja do ustawienia.
     */
    void setCurrent(LocaleConfig localeConfig, boolean isPreffered);
}
