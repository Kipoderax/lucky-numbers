package kipoderax.virtuallotto.commons.dtos.models;

public class HistoryGameDtoBuilder {

    private String dateGame = "";
    private int amountBets = 0;
    private int amountGoalThrees = 0;
    private int amountGoalFours = 0;
    private int amountGoalFives = 0;
    private int amountGoalSixes = 0;
    private int experience = 0;
    private int result = 0;

    public HistoryGameDtoBuilder() {
    }

    public HistoryGameDtoBuilder dateGame(String dateGame) {
        this.dateGame = dateGame;
        return this;
    }

    public HistoryGameDtoBuilder amountBets(int amountBets) {
        this.amountBets = amountBets;
        return this;
    }

    public HistoryGameDtoBuilder amountGoalThrees(int amountGoalThrees) {
        this.amountGoalThrees = amountGoalThrees;
        return this;
    }

    public HistoryGameDtoBuilder amountGoalFours(int amountGoalFours) {
        this.amountGoalFours = amountGoalFours;
        return this;
    }

    public HistoryGameDtoBuilder amountGoalFives(int amountGoalFives) {
        this.amountGoalFives = amountGoalFives;
        return this;
    }

    public HistoryGameDtoBuilder amountGoalSixes(int amountGoalSixes) {
        this.amountGoalSixes = amountGoalSixes;
        return this;
    }

    public HistoryGameDtoBuilder experience(int experience) {
        this.experience = experience;
        return this;
    }

    public HistoryGameDtoBuilder result(int result) {
        this.result = result;
        return this;
    }

    public HistoryGameDto build() {
        return new HistoryGameDto(
                dateGame, amountBets,
                amountGoalThrees, amountGoalFours, amountGoalFives, amountGoalSixes,
                experience, result);
    }
}
