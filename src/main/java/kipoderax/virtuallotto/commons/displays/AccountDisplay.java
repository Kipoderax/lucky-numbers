package kipoderax.virtuallotto.commons.displays;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.forms.HistoryGameForm;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.service.Experience;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.DecimalFormat;

@Service
public class AccountDisplay {

    private UserRepository userRepository;
    private GameRepository gameRepository;

    public AccountDisplay(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public void amountGoal(Model model, HistoryGameForm historyGameForm) {
        int amountBets = historyGameForm.getAmountBets();
        double percentChanceForThrees = ((double) historyGameForm.getAmountGoalThrees() / amountBets) * 100;
        double percentChanceForFours = ((double) historyGameForm.getAmountGoalFours() / amountBets) * 100;
        double percentChanceForFives = ((double) historyGameForm.getAmountGoalFives() / amountBets) * 100;
        double percentChanceForSixes = ((double) historyGameForm.getAmountGoalSixes() / amountBets) * 100;

        model.addAttribute("amountOfThree", historyGameForm.getAmountGoalThrees());
        model.addAttribute("percentChanceForThrees", new DecimalFormat("##.##").format(percentChanceForThrees));

        model.addAttribute("amountOfFour", historyGameForm.getAmountGoalFours());
        model.addAttribute("percentChanceForFours", new DecimalFormat("##.##").format(percentChanceForFours));

        model.addAttribute("amountOfFive", historyGameForm.getAmountGoalFives());
        model.addAttribute("percentChanceForFives", new DecimalFormat("##.###").format(percentChanceForFives));

        model.addAttribute("amountOfSix", historyGameForm.getAmountGoalSixes());
        model.addAttribute("percentChangeForSixes", new DecimalFormat("##.####").format(percentChanceForSixes));

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
        model.addAttribute("expense", historyGameForm.getAmountBets() * 3);
        model.addAttribute("addup", gameRepository.findProfit(username));
        model.addAttribute("result", (gameRepository.findProfit(username) - (historyGameForm.getAmountBets() * 3)));
    }
}
