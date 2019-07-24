package kipoderax.virtuallotto.auth.controllers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Tester {
    public static void main(String[] args) {
        int number = 134;
        Tester test = new Tester();
        for (int i = 1;  i <= 10000; i++) {
            test.allCombination(number);
        }
    }

    public List<Integer> allCombination(int number) {
        List<Integer> list = new ArrayList<>();
        TreeSet<Integer> setOfCombination = new TreeSet<>();
        List<Integer> saveList = new ArrayList<>();
        SecureRandom randomNumber = new SecureRandom();

            while (setOfCombination.size() != 6) {
                setOfCombination.add(randomNumber.nextInt(49) + 1);
            }
            list.addAll(setOfCombination);

        int sum = 0;
        for (int i = 0; i <= 5; i++) {
            sum += list.get(i);
        }
        if (sum == number) {
            saveList.addAll(list);
        }

        if (saveList.size() > 0 ) {
            System.out.println(sum + ": " + saveList);
        }
//        System.out.println(setOfCombination);
//        System.out.println(randomNumber);

        return saveList;
    }
}
