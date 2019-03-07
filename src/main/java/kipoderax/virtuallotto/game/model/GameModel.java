package kipoderax.virtuallotto.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
@Data @NoArgsConstructor @AllArgsConstructor
public class GameModel {

    private Integer[] targetRealVersion = {3, 16, 17, 20 ,25 ,36};
    private Integer[] targetEasyVersion = {2, 5, 8, 10, 12, 18};

    private int number;
    private Set<Integer> numberSet = new TreeSet<>(); //zbior 6 wylosowanych liczb
    private List<Integer> addGoalNumbers = new ArrayList<>(); //zbiór trafionych liczb

    private int count;

}