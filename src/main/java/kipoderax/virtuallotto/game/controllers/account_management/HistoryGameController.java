package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryGameController {

    private UserSession userSession;

    private HistoryGameDtoService historyGameDtoService;
    private StatisticsService statisticsService;
    private UserRepository userRepository;

    public HistoryGameController(HistoryGameDtoService historyGameDtoService,
                                 StatisticsService statisticsService,
                                 UserSession userSession,
                                 UserRepository userRepository) {

        this.historyGameDtoService = historyGameDtoService;
        this.statisticsService = statisticsService;
        this.userSession = userSession;
        this.userRepository = userRepository;
    }

    @GetMapping("/history")
    public String historyGame(Model model) {

        model.addAttribute("history", historyGameDtoService.getAllHistoryGames(userSession.getUser().getId()));

        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault().subList(0, 5));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        return "auth/history-game";
    }
}
