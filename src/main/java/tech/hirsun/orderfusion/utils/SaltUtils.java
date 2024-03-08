package tech.hirsun.orderfusion.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SaltUtils {
    public static String getRandomSalt(int n){
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(System.currentTimeMillis());
            byte[] saltBytes = new byte[n];
            random.nextBytes(saltBytes);
            return Base64.getEncoder().encodeToString(saltBytes).substring(0, n);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getRandomSalt(6));
    }
}

