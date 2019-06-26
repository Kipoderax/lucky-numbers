package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

public class LostAccount {

    private final UserRepository userRepository;

    public LostAccount(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkEmail(String email) {

        return userRepository.existsByEmail(email);
    }

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

    public void sendEmailAfterCheckMail(String email) {

        if (checkEmail(email)) {

            randomStringGenerator();
        }
    }

}
