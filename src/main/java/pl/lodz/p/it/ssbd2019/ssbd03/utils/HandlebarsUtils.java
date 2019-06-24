package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import javax.mvc.Models;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Klasa pomocnicza zawierająca logikę generowania template, korzystajaca ze wzorca fluent api.
 */
public class HandlebarsUtils {

    public static Pooler servletContextLoader(final ServletContext servletContext) {
        return withLoader(new ServletContextTemplateLoader(servletContext));
    }

    public static Pooler withLoader(final TemplateLoader templateLoader) {
        return new Pooler(templateLoader);
    }

    public static class Pooler {
        private final TemplateLoader templateLoader;
        private Models models;

        private Pooler(final TemplateLoader templateLoader) {
            this.templateLoader = templateLoader;
        }

        public Pooler withModels(Models models) {
            this.models = models;
            return this;
        }

        public CompiledTemplate compile(String viewUnparsed) throws IOException {
            final Handlebars handlebars = new Handlebars(templateLoader);

            handlebars.registerHelper("date", new Helper<Timestamp>() {
                public CharSequence apply(Timestamp timestamp, Options options) {
                    return new SimpleDateFormat("HH:mm, dd.MM.yyyy").format(timestamp);
                }
            });

            org.beryx.hbs.Helpers.register(handlebars);
            final Template template = handlebars.compileInline(viewUnparsed);
            return new CompiledTemplate(template, models);
        }
    }

    public static class CompiledTemplate {
        private final Template template;
        private final Models models;

        private CompiledTemplate(final Template template, final Models models) {
            this.template = template;
            this.models = models;
        }

        public String asString() throws IOException {
            return template.apply(models.asMap());
        }

        public void apply(Writer writer) throws IOException {
            template.apply(models.asMap(), writer);
        }
    }

}
