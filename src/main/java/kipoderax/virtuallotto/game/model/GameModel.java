package kipoderax.virtuallotto.game.model;

import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.game.service.ConvertToJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class GameModel {

    public int[] createNumbersOfNumbersForm(NumbersForm numbersForm) {
        int numbers[] = {numbersForm.getNumber1(), numbersForm.getNumber2(), numbersForm.getNumber3(),
                numbersForm.getNumber4(), numbersForm.getNumber5(), numbersForm.getNumber6()};

        return numbers;
    }

    private ConvertToJson convertToJson = new ConvertToJson();

    private int number; //komponent numberSet
    private int experience;
    private int level;

//    convertToJson.getLastWins(convertToJson.getMoneyRew()[0])
    private int[] rewardsMoney = {-3,
            convertToJson.getLastWins(convertToJson.getMoneyRew()[0]),
            convertToJson.getLastWins(convertToJson.getMoneyRew()[1]),
            convertToJson.getLastWins(convertToJson.getMoneyRew()[2]),
            convertToJson.getLastWins(convertToJson.getMoneyRew()[3])};
    private int[] rewardsExperience = {1, 3, 21, 186, 1_985, 15_134};

    private int saldo;
    private int winPerOneGame; //przedstawia zysk dla aktualnej gry

    private StringBuilder dateGame = convertToJson.getLastDate(convertToJson.getDate());
    private List<Integer> lastNumbers = convertToJson.getLastNumbers(convertToJson.getLastLottoNumbers());

    private List<Integer> numberSet = new ArrayList<>(); //zbior 6 wylosowanych liczb
    private List<Integer> addGoalNumbers = new ArrayList<>(); //zbiór trafionych liczb

}