package kipoderax.virtuallotto.game.model;

import kipoderax.virtuallotto.game.service.ConvertToJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data @NoArgsConstructor @AllArgsConstructor
public class GameModel {
    //todo jak to uwzglednic w PostMappingu, w templatce?
//    @Min(0) @Max(200)
    private ConvertToJson convertToJson = new ConvertToJson();

    private int number; //komponent numberSet
    private int[] rewards = {-3, 24, 120, 4_800, 2_000_000};
    private int saldo;
    private int winPerOneGame; //przedstawia zysk dla aktualnej gry

    private List<Integer> testRealNumbers = convertToJson.getNumbers(convertToJson.getLastLottoNumbers());
    private Integer[] targetEasyVersion = {2, 5, 8, 10, 12, 18}; //losowanie z 25

    private Set<Integer> numberSet = new TreeSet<>(); //zbior 6 wylosowanych liczb
    private List<Integer> addGoalNumbers = new ArrayList<>(); //zbiór trafionych liczb

}