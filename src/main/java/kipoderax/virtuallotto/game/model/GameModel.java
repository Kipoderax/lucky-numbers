package kipoderax.virtuallotto.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data @NoArgsConstructor @AllArgsConstructor
public class GameModel {

    private int number; //komponent numberSet
    private int[] rewards = {-3, 24, 120, 4_800, 2_000_000};
    private int saldo;
    private int winPerOneGame; //przedstawia zysk dla aktualnej gry

    private Integer[] targetRealVersion = {3, 16, 17, 20 ,25 ,36}; //losowanie z 49
    private Integer[] targetEasyVersion = {2, 5, 8, 10, 12, 18}; //losowanie z 25

    private Set<Integer> numberSet = new TreeSet<>(); //zbior 6 wylosowanych liczb
    private List<Integer> addGoalNumbers = new ArrayList<>(); //zbiór trafionych liczb

}