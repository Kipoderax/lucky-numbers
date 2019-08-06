package kipoderax.virtuallotto.auth.service;

import java.security.SecureRandom;

public class LostAccount {

    public static String randomStringGenerator() {
        String randomString = "";

        String signs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 30; i++) {

            int number = secureRandom.nextInt(signs.length());
            randomString += signs.substring(number, number + 1);
        }

        return randomString;
    }
}
