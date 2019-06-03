package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.game.model.WinnerBets;

import java.util.List;

public interface WinnerBetsService{

    void addWinnerBetsWith3Numbers(WinnerBets winnerBets);
    void addWinnerBetsWith4Numbers(WinnerBets winnerBets);
    void addWinnerBetsWith5Numbers(WinnerBets winnerBets);
    void addWinnerBetsWith6Numbers(WinnerBets winnerBets);

    List<WinnerBets> getWinnerBetsWith3Numbers();
    List<WinnerBets> getWinnerBetsWith4Numbers();
    List<WinnerBets> getWinnerBetsWith5Numbers();
    List<WinnerBets> getWinnerBetsWith6Numbers();

}
