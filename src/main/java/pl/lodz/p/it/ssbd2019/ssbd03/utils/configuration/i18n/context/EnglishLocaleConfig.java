package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context;

import lombok.EqualsAndHashCode;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.annotations.KnownLocale;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;

@EqualsAndHashCode
@KnownLocale("EN")
@ApplicationScoped
public class EnglishLocaleConfig implements LocaleConfig {
    private final Locale locale = Locale.ENGLISH;
    private final String propertiesFile = "locale_en.properties";

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public String filePath() {
        return LocaleConfig.PROPERTIES_CATALOG + "/" + propertiesFile;
    }
}
