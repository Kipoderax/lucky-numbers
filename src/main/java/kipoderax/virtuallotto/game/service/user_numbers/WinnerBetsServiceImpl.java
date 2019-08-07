package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.commons.forms.NumbersForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WinnerBetsServiceImpl implements WinnerBetsService {

    private List<NumbersForm> with3Numbers = new ArrayList<>();
    private List<NumbersForm> with4Numbers = new ArrayList<>();
    private List<NumbersForm> with5Numbers = new ArrayList<>();
    private List<NumbersForm> with6Numbers = new ArrayList<>();

    @Override
    public void addWinnerBetsWith3Numbers(NumbersForm numbersForm) {
        this.with3Numbers.add(numbersForm);
    }

    @Override
    public void addWinnerBetsWith4Numbers(NumbersForm numbersForm) {
        this.with4Numbers.add(numbersForm);
    }

    @Override
    public void addWinnerBetsWith5Numbers(NumbersForm numbersForm) {
        this.with5Numbers.add(numbersForm);
    }

    @Override
    public void addWinnerBetsWith6Numbers(NumbersForm numbersForm) {
        this.with6Numbers.add(numbersForm);
    }

    @Override
    public List<NumbersForm> getWinnerBetsWith3Numbers() {
        return with3Numbers;
    }

    @Override
    public List<NumbersForm> getWinnerBetsWith4Numbers() {
        return with4Numbers;
    }

    @Override
    public List<NumbersForm> getWinnerBetsWith5Numbers() {
        return with5Numbers;
    }

    @Override
    public List<NumbersForm> getWinnerBetsWith6Numbers() {
        return with6Numbers;
    }
}
