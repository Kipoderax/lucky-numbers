package kipoderax.virtuallotto.commons.dtos.models;

public class NumbersDtoBuilder {

    private int number1 = 0;
    private int number2 = 0;
    private int number3 = 0;
    private int number4 = 0;
    private int number5 = 0;
    private int number6 = 0;

    public NumbersDtoBuilder() {
    }

    public NumbersDtoBuilder number1(int number1) {
        this.number1 = number1;
        return this;
    }

    public NumbersDtoBuilder number2(int number2) {
        this.number2 = number2;
        return this;
    }

    public NumbersDtoBuilder number3(int number3) {
        this.number3 = number3;
        return this;
    }

    public NumbersDtoBuilder number4(int number4) {
        this.number4 = number4;
        return this;
    }

    public NumbersDtoBuilder number5(int number5) {
        this.number5 = number5;
        return this;
    }

    public NumbersDtoBuilder number6(int number6) {
        this.number6 = number6;
        return this;
    }

    public LottoNumbersDto build() {
        return new LottoNumbersDto(number1, number2, number3, number4, number5, number6);
    }

}
