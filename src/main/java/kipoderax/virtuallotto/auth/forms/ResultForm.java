package kipoderax.virtuallotto.auth.forms;

import lombok.Data;

@Data
public class ResultForm {

    int failGoal;
    int goalOneNumber;
    int goal2Numbers;
    int goal3Numbers;
    int goal4Numbers;
    int goal5Numbers;
    int goal6Numbers;
    int totalCostBets;
    int totalEarn;
    int finishResult;
    int totalExp;

}
