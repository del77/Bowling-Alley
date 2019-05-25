package pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.PropertyManager;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.EnglishLocaleConfig;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LocaleConfig;

import javax.servlet.ServletContext;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguageMapFactoryTest {
    @Mock
    private PropertyManager propertyManager;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private LanguageMapFactoryImpl languageMapFactory;

    @Test
    public void getLanguageMapThrowsPropertiesLoadExceptionWhenLoadingError() throws PropertiesLoadException {
        when(propertyManager.loadProperties(any())).thenThrow(PropertiesLoadException.class);
        when(servletContext.getRealPath(any())).thenReturn("");
        Assertions.assertThrows(PropertiesLoadException.class,
                () -> languageMapFactory.getLanguageMap(mock(LocaleConfig.class)));
    }

    @Test
    public void getLanguageMapThrowsPropertiesLoadExceptionRealPathNull() throws PropertiesLoadException {
        when(servletContext.getRealPath(any())).thenReturn(null);
        Assertions.assertThrows(PropertiesLoadException.class,
                () -> languageMapFactory.getLanguageMap(mock(LocaleConfig.class)));
    }

    @Test
    public void getLanguageMapChangesCurrentLoadedLanguage() throws PropertiesLoadException {
        LocaleConfig en = new EnglishLocaleConfig();
        when(propertyManager.loadProperties(any())).thenReturn(new Properties());
        when(servletContext.getRealPath(any())).thenReturn("");
        languageMapFactory.getLanguageMap(en);
        Assertions.assertEquals(en, languageMapFactory.getLastLoadedLocaleConfig());
    }
}