package kipoderax.virtuallotto.commons.forms;

import lombok.Data;

@Data
public class ResultForm {

    private int failGoal;
    private int goalOneNumber;
    private int goal2Numbers;
    private int goal3Numbers;
    private int goal4Numbers;
    private int goal5Numbers;
    private int goal6Numbers;
    private int totalCostBets;
    private int totalEarn;
    private int finishResult;
    private int totalExp;

}
