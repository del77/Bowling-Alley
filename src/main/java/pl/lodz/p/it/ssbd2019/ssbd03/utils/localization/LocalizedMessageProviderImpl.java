package pl.lodz.p.it.ssbd2019.ssbd03.utils.localization;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.LanguageMapFactory;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LanguageContext;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Klasa, któej obiekty na podstawie lokalizacji użytkownika pobierają odpowiednie elementy z bazy
 * klucz-wartość. Klasa przystosowana jest do potrzeb warstwy widoku.
 */
@SessionScoped
public class LocalizedMessageProviderImpl implements Serializable, LocalizedMessageProvider {
    private static final long serialVersionUID = 1L;

    @Inject
    private LanguageMapFactory languageMapFactory;
    @Inject
    private LanguageContext languageContext;

    /**
     * Zwraca łańcuch tekstowy na podstawie klucza oraz obecnego kontekstu językowego.
     * W przypadku, gdy nie powiedzie się pobranie, zwraca pusty łańcuch.
     * @param key klucz, którego wartość chcemy pobrać
     * @return wartość zlokalizowana
     */
    @Override
    public String get(String key) {
        try {
            return (String)languageMapFactory.getLanguageMap(languageContext.getCurrent()).get(key);
        } catch (PropertiesLoadException | NullPointerException e) {
            return "";
        }
    }
}
