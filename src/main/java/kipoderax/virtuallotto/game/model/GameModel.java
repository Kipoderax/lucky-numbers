package kipoderax.virtuallotto.game.model;

import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import kipoderax.virtuallotto.game.service.ConvertToJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class GameModel {

    public int[] createNumbersOfNumbersForm(LottoNumbersDto lottoNumbersDto) {
        int numbers[] = {lottoNumbersDto.getNumber1(), lottoNumbersDto.getNumber2(), lottoNumbersDto.getNumber3(),
                lottoNumbersDto.getNumber4(), lottoNumbersDto.getNumber5(), lottoNumbersDto.getNumber6()};

        return numbers;
    }

    private ConvertToJson convertToJson = new ConvertToJson();

    private int number;
    private int experience;
    private int level;

    private int[] rewardsMoney = {-3,
            convertToJson.getLastWins(convertToJson.getMoneyRew()[0]),
            convertToJson.getLastWins(convertToJson.getMoneyRew()[1]),
            convertToJson.getLastWins(convertToJson.getMoneyRew()[2]),
            convertToJson.getLastWins(convertToJson.getMoneyRew()[3])};
    private int[] rewardsExperience = {1, 3, 21, 186, 1_985, 15_134};

    private int saldo;
    private int winPerOneGame;

    private List<String> dateGame = convertToJson.getLastDate(convertToJson.getDate());
    private List<Integer> lastNumbers = convertToJson.getLastNumbers(convertToJson.getLastLottoNumbers());

    private List<Integer> numberSet = new ArrayList<>();
    private List<Integer> addGoalNumbers = new ArrayList<>();

}