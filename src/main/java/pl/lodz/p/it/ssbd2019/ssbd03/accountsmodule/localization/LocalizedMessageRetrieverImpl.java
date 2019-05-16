package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.localization;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.LanguageMapFactory;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LanguageContext;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class LocalizedMessageRetrieverImpl implements Serializable, LocalizedMessageRetriever {
    private static final long serialVersionUID = 1L;

    @Inject
    private LanguageMapFactory languageMapFactory;
    @Inject
    private LanguageContext languageContext;

    @Override
    public String get(String key) {
        try {
            return (String)languageMapFactory.getLanguageMap(languageContext.getCurrent()).get(key);
        } catch (PropertiesLoadException | NullPointerException e) {
            return "";
        }
    }
}
