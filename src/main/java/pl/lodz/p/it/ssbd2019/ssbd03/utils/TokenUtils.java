package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
     * @param hours liczba godzin
     * @return token
     */
    public static Timestamp getValidity(int hours) {
        long currentTimeMillis = System.currentTimeMillis();
        long hoursToMilis = TimeUnit.HOURS.toMillis(hours);
        return new Timestamp(currentTimeMillis + hoursToMilis);
    }

    /**
     * Sprawdza czy token jest ważny
     *
     * @param validity ważność tokenu
     * @return true jeśli token jest ważny
     */
    public static boolean isValid(Timestamp validity) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        return validity.after(current);
    }
}