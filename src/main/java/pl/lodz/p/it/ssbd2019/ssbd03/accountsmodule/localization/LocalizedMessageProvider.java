package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.localization;

public interface LocalizedMessageProvider {
    /**
     * Służy do odczytywania zlokalizowanych wiadomości z resources po kluczu
     *
     * @param key klucz wiadomości
     * @return zlokalizowana wiadomość
     */
    String get(String key);
}
