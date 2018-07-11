/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Djan Sarwari
 */
public class PasswordEncryption {

    private static final String secret = "994D7D80-9F0F-4911-BF4C-F8D8666721D9:9F4C36FE-5D8A-483E";
    private static String result;

    public static String encrypt(String rawPassword) {
        try {
            Mac hmacSHA128 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(),
                    "HmacSHA256");
            hmacSHA128.init(secretKeySpec);

            byte[] digest = hmacSHA128.doFinal(rawPassword.getBytes());
            BigInteger hash = new BigInteger(1, digest);
            result = hash.toString(16);
            if ((result.length() % 2) != 0) {
                result = "0" + result;
            }
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException ex) {
            throw new RuntimeException("Problemas calculando HMAC", ex);
        }
        return result;
    }
}
