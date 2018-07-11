/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.util;

/**
 *
 * @author Djan Sarwari
 */
public class EncryptedPassword {

    private final String encryptedPassword;

    public static EncryptedPassword forRawPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return new EncryptedPassword(PasswordEncryption.encrypt(rawPassword));
    }

    public static EncryptedPassword forEncryptedPassword(String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }
        return new EncryptedPassword(encryptedPassword);
    }

    private EncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEncrypted() {
        return encryptedPassword;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof EncryptedPassword) {
            EncryptedPassword thatPassword = (EncryptedPassword) that;
            return thatPassword.encryptedPassword.equals(encryptedPassword);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (encryptedPassword == null) {
            return 1;
        }
        return encryptedPassword.hashCode();
    }
}
