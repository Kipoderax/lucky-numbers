package kipoderax.virtuallotto.commons.validation;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

@Data
public class InputNumberValidation {

    @Value("${game.duplicateNumbers}")
    private String duplicateNumbersExist;

    public boolean isDifferentNumberPairs(int[] number) {

        for (int i = 0; i < number.length; i++) {
            for (int j = i + 1; j < number.length; j++) {
                if (number[i] == number[j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean rangeNumbers(int[] number) {

        for (int i = 0; i < number.length; i++) {
            if (number[i] < 1 || number[i] > 49) {
                return false;
            }
        }

        return true;
    }

    public boolean isInputNumbersCorrect(int[] number, Model model) {
        if (!isDifferentNumberPairs(number)) {

            model.addAttribute("range", duplicateNumbersExist);
            if (rangeNumbers(number)) {
                return false;
            }
            return false;
        }

        return true;
    }

    public void sort(int[] tab) {

        for (int i = 0; i < tab.length; i++) {
            for (int j = i + 1; j < tab.length; j++) {
                if (tab[i] > tab[j]) {
                    swap(tab, i, j);
                }
            }
        }
    }

    public void swap(int[] tab, int lower, int higher) {

        int temp = tab[lower];
        tab[lower] = tab[higher];
        tab[higher] = temp;
    }
}
