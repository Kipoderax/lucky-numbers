package kipoderax.virtuallotto.commons.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class InputNumberValidationTest {

    private InputNumberValidation inputNumberValidation;
    private int[] number1, number2, number3;

    @BeforeEach
    void setUp() {
        inputNumberValidation = new InputNumberValidation();
        number1 = new int[]{13, 1, 8, 24, 11, 5};
        number2 = new int[]{13, 1, 13, 8, 5, 11};
        number3 = new int[]{13, 1, 80, 24, 11, 5};
    }

    @Test
    void testIsDifferentNumberPairs() {
        assertThat(inputNumberValidation.isDifferentNumberPairs(number1)).isTrue();
        assertThat(inputNumberValidation.isDifferentNumberPairs(number2)).isFalse();
    }

    @Test
    void testRangeNumbersFrom1To49() {
        assertThat(inputNumberValidation.rangeNumbers(number1)).isTrue();
        assertThat(inputNumberValidation.rangeNumbers(number3)).isFalse();
    }

    @Test
    void testSort() {
        //given
        int[] numberInDescOrder = {1, 5, 8, 11, 13, 24};
        int[] numberNotDescOrder = {1, 8, 5, 11, 13, 24};

        //when
        inputNumberValidation.sort(number1);

        //then
        assertThat(number1).isEqualTo(numberInDescOrder);
        assertThat(number1).isNotEqualTo(numberNotDescOrder);
    }

    @Test
    void swap() {
        //given
        int higher = 0;
        int lower = 1;
        int[] number1 = {7, 4};

        //before when
        assertAll(
                () -> assertThat(number1[0]).isEqualTo(7),
                () -> assertThat(number1[1]).isEqualTo(4)
        );

        //when
        inputNumberValidation.swap(number1, lower, higher);

        //then
        assertAll(
                () -> assertThat(number1[0]).isEqualTo(4),
                () -> assertThat(number1[1]).isEqualTo(7)
        );
    }
}