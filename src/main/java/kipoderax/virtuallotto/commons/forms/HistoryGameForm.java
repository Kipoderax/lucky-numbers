package kipoderax.virtuallotto.commons.forms;

import lombok.Data;

@Data
public class HistoryGameForm {

    private int historyGameId;
    private StringBuilder dateGame;
    private int amountBets;
    private int amountGoalThrees;
    private int amountGoalFours;
    private int amountGoalFives;
    private int amountGoalSixes;
    private int experience;
    private int result;

}
