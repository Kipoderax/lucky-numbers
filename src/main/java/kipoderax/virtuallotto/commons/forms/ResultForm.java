package kipoderax.virtuallotto.commons.forms;

import lombok.Data;

import java.util.List;

@Data
public class ResultForm {

    NumbersForm numbersForm = new NumbersForm();

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

    private List<Integer> goalBets3Numbers;
    private List<Integer> goalBets4Numbers;
    private List<Integer> goalBets5Numbers;
    private List<Integer> goalBets6Numbers;

    private List<List<Integer>> goalWith3Numbers;
    private List<List<Integer>> goalWith4Numbers;
    private List<List<Integer>> goalWith5Numbers;
    private List<List<Integer>> goalWith6Numbers;

}
