package pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.MessageNotSentException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.PropertyManager;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.ClassicMessage;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;

import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.ejb.Singleton;
import javax.servlet.ServletContext;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Klasa, której obiekt odpowiedzialny jest za wysyłanie wiadomości e-mail do użytkowników.
 */
@Singleton
public class EmailMessenger implements Messenger {

    private static final String CLASSPATH_MAIL_PROPERTIES = "/WEB-INF/classes/mail.properties";

    @Inject
    private PropertyManager propertyManager;

    @Inject
    private ServletContext servletContext;

    private Mailer mailer;

    /**
     * Metoda odpowiedzialna za wysyłanie wiadomości. Zwróć uwagę na format wiadomości.
     *
     * @param message obiekt wiadomości.
     * @throws MessageNotSentException w przypadku gdy nie uda się połączyć lub wczytać danych do połączenia,
     *                                 rzuca też w wypadku, gdy nie powiedzie się sama akcja wysłania, przykład: zły adres e-mail w "to" albo w "from".
     */
    @Override
    @Asynchronous
    public void sendMessage(ClassicMessage message) throws MessageNotSentException {
        synchronized (this) {
            if (mailer == null) {
                try {
                    MailConfig mailConfig = loadPropertiesConfig();
                    this.mailer = MailerBuilder
                            .withSMTPServer(mailConfig.host, mailConfig.port, mailConfig.user, mailConfig.password)
                            .withTransportStrategy(TransportStrategy.SMTP_TLS)
                            .buildMailer();
                } catch (PropertiesLoadException e) {
                    throw new MessageNotSentException("Message could not be sent, " +
                            "because of configuration loading failure. ", e);
                }
            }
        }
        mailer.sendMail(rebuildIntoMail(message));
    }


    private Email rebuildIntoMail(ClassicMessage classicMessage) {
        return EmailBuilder
                .startingBlank()
                .from(classicMessage.getFrom())
                .to(classicMessage.getTo())
                .withSubject(classicMessage.getSubject())
                .withHTMLText(classicMessage.getBody())
                .buildEmail();
    }

    private MailConfig loadPropertiesConfig() throws PropertiesLoadException {
        final String realPath = servletContext.getRealPath(CLASSPATH_MAIL_PROPERTIES);
        if (realPath == null) {
            throw new PropertiesLoadException("Couldn't retrieve real path of element.");
        }
        final Properties properties = propertyManager.loadProperties(Paths.get(realPath));
        return extractProperties(properties);
    }

    private MailConfig extractProperties(Properties properties) throws PropertiesLoadException {
        final MailConfig mailConfig = new MailConfig();
        try {
            mailConfig.host = properties.getProperty("smtp.server.host");
            mailConfig.port = Integer.parseInt(properties.getProperty("smtp.server.port"));
            mailConfig.user = properties.getProperty("smtp.server.user");
            mailConfig.password = properties.getProperty("smtp.server.password");
            return mailConfig;
        } catch (final Exception e) {
            throw new PropertiesLoadException("Could not load mail configuration. ", e);
        }
    }

    private class MailConfig {
        private String host;
        private int port;
        private String user;
        private String password;
    }
}
