package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.MessageInterpolator;
import java.util.Hashtable;
import java.util.Locale;

@ApplicationScoped
public class I18nMessageInterpolator implements MessageInterpolator {

    @Inject
    private I18nManager manager;

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        return interpolate(messageTemplate, context, manager.getCurrentLocale());
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        Message message = extractMessage(messageTemplate);
        System.out.println(message);
        if (message.isCoded) {
            if (manager.getCurrentLocale().equals(locale)) {
                try {
                    Hashtable<Object, Object> languageMap = manager.getLanguageMap();
                    return (String)languageMap.get(message.body);
                } catch (PropertiesLoadException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Hashtable<Object, Object> newLanguageMap = manager.changeLanguage(locale);
                    return  (String)newLanguageMap.get(message.body);
                } catch (PropertiesLoadException e) {
                    e.printStackTrace();
                }
            }
        }
        return message.body;
    }

    public Locale getDefaultLocale() {
        return manager.getCurrentLocale();
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
