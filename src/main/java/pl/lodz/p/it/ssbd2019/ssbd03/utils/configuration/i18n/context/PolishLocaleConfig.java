package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context;

import lombok.EqualsAndHashCode;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.annotations.KnownLocale;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;

@EqualsAndHashCode
@KnownLocale("PL")
@ApplicationScoped
public class PolishLocaleConfig implements LocaleConfig {
    private final Locale locale = new Locale("pl", "PL");
    private final String propertiesFile = "locale.properties";

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public String filePath() {
        return LocaleConfig.PROPERTIES_CATALOG + "/" + propertiesFile;
    }
}
