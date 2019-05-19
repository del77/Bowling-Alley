package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LanguageContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.MessageInterpolator;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Klasa interpolatora obsługująca interpolację dla stringów przy walidacji.
 * Implementacja wykorzystuje LanguageMapFactory do wczytywania powiązań.
 * @see LanguageMapFactory
 */
@ApplicationScoped
public class I18nMessageInterpolator implements MessageInterpolator {

    @Inject
    private LanguageMapFactory manager;

    @Inject
    private LanguageContext languageContext;

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        return interpolate(messageTemplate, context, manager.getLastLoadedLocaleConfig().locale());
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        Message message = extractMessage(messageTemplate);
        if (message.isCoded) {
            try {
                Hashtable<Object, Object> newLanguageMap =
                        manager.getLanguageMap(languageContext.getLocaleConfig(locale.getLanguage()));
                return  (String)newLanguageMap.get(message.body);
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
            }
        }
        return message.body;
    }

    private Message extractMessage(String coded) {
        Message message = new Message();
        if (coded.matches("^[{](.*)[}]")) {
            message.isCoded = true;
            message.body = coded
                    .replaceAll("[{]", "")
                    .replaceAll("[}]", "");
            return message;
        }
        message.body = coded;
        return message;
    }

    private class Message {
        private boolean isCoded = false;
        private String body;
    }
}
