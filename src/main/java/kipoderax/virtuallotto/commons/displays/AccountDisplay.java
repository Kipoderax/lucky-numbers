package kipoderax.virtuallotto.commons.displays;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.forms.HistoryGameForm;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.service.Experience;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AccountDisplay {

    private UserRepository userRepository;
    private GameRepository gameRepository;

    public AccountDisplay(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public void amountGoal(Model model, HistoryGameForm historyGameForm) {

        model.addAttribute("amountOfThree", historyGameForm.getAmountGoalThrees());
        model.addAttribute("amountOfFour", historyGameForm.getAmountGoalFours());
        model.addAttribute("amountOfFive", historyGameForm.getAmountGoalFives());
        model.addAttribute("amountOfSix", historyGameForm.getAmountGoalSixes());
        model.addAttribute("numberGame", historyGameForm.getAmountBets());
        model.addAttribute("exp", historyGameForm.getExperience());
    }

    public void mainInformation(Model model, String username, int userId, Experience experience,
                                HistoryGameForm historyGameForm) {

        model.addAttribute("player", userRepository.findUsernameByUsername(username));
        model.addAttribute("username", userRepository.findUsernameByUsername(username));
        model.addAttribute("email", userRepository.findEmailByUserId(userId));
        model.addAttribute("createAccount", userRepository.findDateOfCreateAccountByUserId(username));
        model.addAttribute("lastLogin", userRepository.findLastLoginDateByUserId(username));
        model.addAttribute("saldo", userRepository.findSaldoByUserId(userId));
        model.addAttribute("level", experience.currentLevel(historyGameForm.getExperience()));
        model.addAttribute("toNextLevel", experience.needExpToNextLevel(historyGameForm.getExperience()));
        model.addAttribute("onehundred", experience.needExpForAllLevel(historyGameForm.getExperience()));
    }

    public void statisticsInformation(Model model, String username, HistoryGameForm historyGameForm) {
        model.addAttribute("expense", historyGameForm.getExperience() * 3);
        model.addAttribute("addup", gameRepository.findProfit(username));
        model.addAttribute("result", (gameRepository.findProfit(username) - (historyGameForm.getExperience() * 3)));
    }
}
