package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.game.model.WinnerBets;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WinnerBetsServiceImpl implements WinnerBetsService {

    private List<WinnerBets> with3Numbers = new ArrayList<>();
    private List<WinnerBets> with4Numbers = new ArrayList<>();
    private List<WinnerBets> with5Numbers = new ArrayList<>();
    private List<WinnerBets> with6Numbers = new ArrayList<>();

    @Override
    public void addWinnerBetsWith3Numbers(WinnerBets winnerBets) {

        this.with3Numbers.add(winnerBets);
    }

    @Override
    public void addWinnerBetsWith4Numbers(WinnerBets winnerBets) {
        this.with4Numbers.add(winnerBets);
    }

    @Override
    public void addWinnerBetsWith5Numbers(WinnerBets winnerBets) {
        this.with5Numbers.add(winnerBets);
    }

    @Override
    public void addWinnerBetsWith6Numbers(WinnerBets winnerBets) {
        this.with6Numbers.add(winnerBets);
    }

    @Override
    public List<WinnerBets> getWinnerBetsWith3Numbers() {
        return with3Numbers;
    }

    @Override
    public List<WinnerBets> getWinnerBetsWith4Numbers() {
        return with4Numbers;
    }

    @Override
    public List<WinnerBets> getWinnerBetsWith5Numbers() {
        return with5Numbers;
    }

    @Override
    public List<WinnerBets> getWinnerBetsWith6Numbers() {
        return with6Numbers;
    }
}
