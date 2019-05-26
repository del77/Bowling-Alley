package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.TextParsingException;

import java.security.NoSuchAlgorithmException;

public class SHA256ProviderTest {

    @Test
    public void shouldThrowExceptionOnTextNull()  {
        Assertions.assertThrows(TextParsingException.class, () -> SHA256Provider.encode(null));
    }

    @Test
    public void shouldEncodeGivenTextRight() throws SsbdApplicationException {
        String givenText = "123", encoded = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
        String result = "";
        result = SHA256Provider.encode(givenText);
        Assertions.assertEquals(encoded, result);
    }
}
