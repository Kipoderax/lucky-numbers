package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BetController {

     private GameService gameService;
     private UserService userService;

     private UserSession userSession;

    public BetController(GameService gameService, UserService userService, UserSession userSession) {
        this.gameService = gameService;
        this.userService = userService;

        this.userSession = userSession;
    }

    @GetMapping("/zaklad")
    public String bet(Model model, Game game, User user) {
        GameModel gameModel = new GameModel();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        model.addAttribute("target", gameService.showTarget());
        model.addAttribute("wylosowane", gameService.generateNumber(gameModel));
        model.addAttribute("trafione", gameService.addGoalNumber(gameModel, user.getLogin(), game.getSaldo()));
        model.addAttribute("saldo", userService.getSaldo(user.getLogin(), game.getSaldo()));

        return "game/bet";
    }
}