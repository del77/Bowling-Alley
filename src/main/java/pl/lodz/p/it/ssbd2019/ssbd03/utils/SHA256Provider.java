package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TextParsingException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Provider {
    public static String encode(String text) throws NoSuchAlgorithmException, TextParsingException {
        if(text == null)
            throw new TextParsingException("Given text was null.");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHashBytes = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
        BigInteger bigInt = new BigInteger(1, encodedHashBytes);
        return String.format("%0" + (encodedHashBytes.length << 1) + "x", bigInt);
    }
}
