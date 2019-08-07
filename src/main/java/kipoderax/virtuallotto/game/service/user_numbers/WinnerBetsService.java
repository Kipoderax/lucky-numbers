package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.commons.forms.NumbersForm;

import java.util.List;

public interface WinnerBetsService{

    void addWinnerBetsWith3Numbers(NumbersForm numbersForm);
    void addWinnerBetsWith4Numbers(NumbersForm numbersForm);
    void addWinnerBetsWith5Numbers(NumbersForm numbersForm);
    void addWinnerBetsWith6Numbers(NumbersForm numbersForm);

    List<NumbersForm> getWinnerBetsWith3Numbers();
    List<NumbersForm> getWinnerBetsWith4Numbers();
    List<NumbersForm> getWinnerBetsWith5Numbers();
    List<NumbersForm> getWinnerBetsWith6Numbers();

}
