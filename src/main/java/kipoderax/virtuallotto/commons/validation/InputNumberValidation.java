package kipoderax.virtuallotto.commons.validation;

public class InputNumberValidation {

    public boolean isDifferentNumberPairs (int[] number) {

        for (int i = 0; i < number.length; i++) {
            for (int j = i + 1; j < number.length; j++) {
            if (number[i] == number[j]) {
                return false;
            }
        }
    }

        return true;
    }

    public boolean rangeNumbers ( int[] number){

        for (int value : number) {
            if (value < 1 || value > 49) {
                return false;
            }
        }

        return true;
    }

    public void sort ( int[] tab){

        for (int i = 0; i < tab.length; i++) {
            for (int j = i + 1; j < tab.length; j++) {
                if (tab[i] > tab[j]) {
                    swap(tab, i, j);
                }
            }
        }
    }

    public void swap ( int[] tab, int lower, int higher){

        int temp = tab[lower];
        tab[lower] = tab[higher];
        tab[higher] = temp;
    }

}