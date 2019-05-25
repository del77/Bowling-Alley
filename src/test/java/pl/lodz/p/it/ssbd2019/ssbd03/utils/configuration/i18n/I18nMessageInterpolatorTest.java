package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LanguageContext;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LocaleConfig;

import java.util.Locale;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class I18nMessageInterpolatorTest {

    @Mock
    private LanguageMapFactory mapFactory;

    @Mock
    private LanguageContext languageContext;

    @InjectMocks
    I18nMessageInterpolator messageInterpolator;

    @Test
    public void shouldInterpolateReturnsNullMessageThatIsExtractedAndDoesntExist() throws PropertiesLoadException {
        final String message = "{test}";
        when(languageContext.getLocaleConfig(any())).thenReturn(mock(LocaleConfig.class));
        when(mapFactory.getLanguageMap(any())).thenReturn(new Properties());
        String interpolated = messageInterpolator.interpolate(message, null, Locale.ENGLISH);
        Assertions.assertEquals(null, interpolated);
    }

    @Test
    public void shouldInterpolateReturnsMessageThatIsExtractedAndExists() throws PropertiesLoadException {
        final String message = "{test}";
        Properties properties = new Properties();
        String exptectedValue = "value interpolated";
        properties.put("test", exptectedValue);
        when(languageContext.getLocaleConfig(any())).thenReturn(mock(LocaleConfig.class));
        when(mapFactory.getLanguageMap(any())).thenReturn(properties);
        String interpolated = messageInterpolator.interpolate(message, null, Locale.ENGLISH);
        Assertions.assertEquals(exptectedValue, interpolated);
    }

    @Test
    public void shouldInterpolateReturnsMessageThatIsPlain() {
        final String message = "test";
        String interpolated = messageInterpolator.interpolate(message, null, null);
        Assertions.assertEquals("test", interpolated);
    }
}