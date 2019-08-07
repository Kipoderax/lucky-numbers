package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryGameController {

    private UserSession userSession;
    private MainPageDisplay mainPageDisplay;
    private HistoryGameDtoService historyGameDtoService;

    public HistoryGameController(HistoryGameDtoService historyGameDtoService,
                                 MainPageDisplay mainPageDisplay,
                                 UserSession userSession) {

        this.historyGameDtoService = historyGameDtoService;
        this.mainPageDisplay = mainPageDisplay;
        this.userSession = userSession;
    }

    @GetMapping("/history")
    public String historyGame(Model model) {

        model.addAttribute("history", historyGameDtoService.getAllHistoryGames(userSession.getUser().getId()));
        mainPageDisplay.displayGameStatus(model);

        return "auth/history-game";
    }
}
