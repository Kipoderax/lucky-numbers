package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.commons.forms.HistoryGameForm;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private HistoryGameRepository historyGameRepository;

    public AccountService(HistoryGameRepository historyGameRepository) {
        this.historyGameRepository = historyGameRepository;
    }

    public void setDefaultValues(HistoryGameForm historyGameForm, String username) {
        amountBets(historyGameForm, username);
        amountGoalThrees(historyGameForm, username);
        amountGoalFours(historyGameForm, username);
        amountGoalFives(historyGameForm, username);
        amountGoalSixes(historyGameForm, username);
        experience(historyGameForm, username);
    }

    public void experience(HistoryGameForm historyGameForm, String username) {
        if (historyGameRepository.userExperience(username) != null) {
            historyGameForm.setExperience( historyGameRepository.userExperience(username) );
        } else { historyGameForm.setExperience(0); }
    }

    public void amountGoalSixes(HistoryGameForm historyGameForm, String username) {
        if (historyGameRepository.amountGoalSixes(username) != null) {
            historyGameForm.setAmountGoalSixes( historyGameRepository.amountGoalSixes(username) );
        } else { historyGameForm.setAmountGoalSixes(0); }
    }

    public void amountGoalFives(HistoryGameForm historyGameForm, String username) {
        if (historyGameRepository.amountGoalFives(username) != null) {
            historyGameForm.setAmountGoalFives( historyGameRepository.amountGoalFives(username) );
        } else { historyGameForm.setAmountGoalFives(0); }
    }

    public void amountGoalFours(HistoryGameForm historyGameForm, String username) {
        if (historyGameRepository.amountGoalFours(username) != null) {
            historyGameForm.setAmountGoalFours( historyGameRepository.amountGoalFours(username) );
        } else { historyGameForm.setAmountGoalFours(0); }
    }

    public void amountGoalThrees(HistoryGameForm historyGameForm, String username) {
        if (historyGameRepository.amountGoalThrees(username) != null) {
            historyGameForm.setAmountGoalThrees( historyGameRepository.amountGoalThrees(username) );
        } else { historyGameForm.setAmountGoalThrees(0); }
    }

    public void amountBets(HistoryGameForm historyGameForm, String username) {
        if (historyGameRepository.amountBets(username) != null) {
            historyGameForm.setAmountBets(historyGameRepository.amountBets(username));
        } else { historyGameForm.setAmountBets(0); }
    }
}
