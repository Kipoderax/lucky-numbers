package kipoderax.virtuallotto.commons.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class HistoryGameForm {

    private int historyGameId;
    private String dateGame;
    private int amountBets;
    private int amountGoalThrees;
    private int amountGoalFours;
    private int amountGoalFives;
    private int amountGoalSixes;
    private int experience;
    private int result;

}
