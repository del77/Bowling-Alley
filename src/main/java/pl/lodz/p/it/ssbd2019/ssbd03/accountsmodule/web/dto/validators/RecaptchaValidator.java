package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RecaptchaValidationException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.RecaptchaKeysProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class RecaptchaValidator {

    @Inject
    private RecaptchaKeysProvider recaptchaKeysProvider;

    private static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String VALIDATION_UNSUCCESSFUL = "Validation unsuccessful";

    public void validateCaptcha(String gRecaptchaResponse) throws RecaptchaValidationException {
        if (gRecaptchaResponse == null || gRecaptchaResponse.equals("")) {
            throw new RecaptchaValidationException(VALIDATION_UNSUCCESSFUL);
        }

        try {
            Response response = getValidationResponse(gRecaptchaResponse);
            if (response.getStatus() != 200) {
                throw new RecaptchaValidationException(VALIDATION_UNSUCCESSFUL);
            }

            String stringResponse = response.readEntity(String.class);
            JsonNode jsonNode = new ObjectMapper().readTree(stringResponse);

            if(!jsonNode.get("success").asBoolean()) {
                throw new RecaptchaValidationException(VALIDATION_UNSUCCESSFUL);
            }
        } catch (Exception e) {
            throw new RecaptchaValidationException("Could not validate recaptcha", e);
        }
    }

    private Response getValidationResponse(String gRecaptchaResponse) throws PropertiesLoadException {
        String secret = recaptchaKeysProvider.getSecretKey();
        WebTarget webTarget = ClientBuilder.newClient()
                .target(URL)
                .queryParam("secret", secret)
                .queryParam("response", gRecaptchaResponse);
        Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .accept("application/ld+json")
                .get();

        return response;
    }
}
