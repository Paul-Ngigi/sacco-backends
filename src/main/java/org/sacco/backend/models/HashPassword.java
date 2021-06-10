package org.sacco.backend.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.jboss.logging.Logger;

public class HashPassword {

    private Logger logger =
        Logger.getLogger(HashPassword.class);

    public String getHashToken(final String token) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();

        try {
            MessageDigest mDigestMD5 =
                MessageDigest.getInstance("MD5");
            mDigestMD5.update(token.getBytes());
            byte[] bytesArray = mDigestMD5.digest();
            for(byte b: bytesArray) {
                sb.append(String.format("%02x", b));
            }

            MessageDigest mDigestSHA256 =
                MessageDigest.getInstance("SHA-256");
            mDigestSHA256.update(sb.toString().getBytes());
            byte[] newBytes = mDigestSHA256.digest();
            for(byte b: newBytes) {
                sb2.append(String.format("%02x", b));
            }


            MessageDigest mDigestSHA512 =
            MessageDigest.getInstance("SHA-512");
            mDigestSHA512.update(sb.toString().getBytes());
            byte[] newBytes512 = mDigestSHA512.digest();
            for(byte b: newBytes512) {
                sb3.append(String.format("%02x", b));
            }

        return sb3.toString();
        } catch (NoSuchAlgorithmException e) {
            this.logger.error(e.getLocalizedMessage(),
                e.getCause());
        }
        return null;
    }

}
