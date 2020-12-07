package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Encrypt {

    public static String sha256EncryptSalt(String encryptStr, String salt) {
        MessageDigest md = null;
        String encryptCode = null;

        byte[] bt = (encryptStr + salt).getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            encryptCode = byteToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return encryptCode;
    }

    private static String byteToHex(final byte[] hash) {
        try (final Formatter formatter = new Formatter()) {
            for (byte b : hash)
                formatter.format("%02x", b);
            return formatter.toString();
        }
    }
}
