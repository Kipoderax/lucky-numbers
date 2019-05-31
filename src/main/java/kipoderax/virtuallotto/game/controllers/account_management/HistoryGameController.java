package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryGameController {

    private HistoryGameDtoService historyGameDtoService;
    private UserSession userSession;

    public HistoryGameController(HistoryGameDtoService historyGameDtoService, UserSession userSession) {
        this.historyGameDtoService = historyGameDtoService;
        this.userSession = userSession;
    }

    @GetMapping("/history")
    public String historyGame(Model model) {

        model.addAttribute("history", historyGameDtoService.getAllHistoryGames(userSession.getUser().getId()));

        return "auth/history-game";
    }
}
