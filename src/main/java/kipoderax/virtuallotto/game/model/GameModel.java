package kipoderax.virtuallotto.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data @NoArgsConstructor @AllArgsConstructor
public class GameModel {

    private Integer[] target = {1, 6, 11, 14 ,15 ,18};
    private int number;
    private Set<Integer> numberSet = new TreeSet<>(); //zbior 6 wylosowanych liczb
    private List<Integer> addGoalNumbers = new ArrayList<>(); //zbiór trafionych liczb

    private int count;

}