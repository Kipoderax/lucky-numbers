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
        if (historyGameRepository.amountBets(username) != null) {
            historyGameForm.setAmountBets(historyGameRepository.amountBets(username));
        } else { historyGameForm.setAmountBets(0); }


        if (historyGameRepository.amountGoalThrees(username) != null) {
            historyGameForm.setAmountGoalThrees( historyGameRepository.amountGoalThrees(username) );
        } else { historyGameForm.setAmountGoalThrees(0); }

        if (historyGameRepository.amountGoalFours(username) != null) {
            historyGameForm.setAmountGoalFours( historyGameRepository.amountGoalFours(username) );
        } else { historyGameForm.setAmountGoalFours(0); }

        if (historyGameRepository.amountGoalFives(username) != null) {
            historyGameForm.setAmountGoalFives( historyGameRepository.amountGoalFives(username) );
        } else { historyGameForm.setAmountGoalFives(0); }

        if (historyGameRepository.amountGoalSixes(username) != null) {
            historyGameForm.setAmountGoalSixes( historyGameRepository.amountGoalSixes(username) );
        } else { historyGameForm.setAmountGoalSixes(0); }

        if (historyGameRepository.userExperience(username) != null) {
            historyGameForm.setExperience( historyGameRepository.userExperience(username) );
        } else { historyGameForm.setExperience(0); }
    }
}
