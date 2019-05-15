package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TextParsingException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {
    /**
     * Generuje losowy token
     *
     * @return token
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }

    /**
     * Zwraca czas ważności tokenu
     *
     * @param hours ilość godzin
     * @return token
     */
    public static Timestamp getValidity(int hours) {
        long currentTimeMillis = System.currentTimeMillis();
        int oneDayMillis = 1000 * 60 * 60 * hours;
        return new Timestamp(currentTimeMillis + oneDayMillis);
    }

    /**
     * Sprawdza czy token jest ważny
     *
     * @param validity ważność tokenu
     * @return true jeśli token jest ważny
     */
    public static boolean isValid(Timestamp validity) {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        return validity.getTime() > current.getTime();
    }
}
