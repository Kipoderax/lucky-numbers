package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.model.GameNoEntity;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BetController {

     private GameService gameService;
     private UserService userService;

     private UserSession userSession;
     private GameNoEntity games;

    public BetController(GameService gameService, UserService userService, UserSession userSession,
                         GameNoEntity games) {
        this.gameService = gameService;
        this.userService = userService;

        this.userSession = userSession;
        this.games = games;
    }

    @GetMapping("/zaklad")
    public String bet(Model model) {
        GameModel gameModel = new GameModel();

//        if (!userSession.isUserLogin()) {
//
//            return "redirect:/login";
//        }

        model.addAttribute("target", gameService.showTarget());
        model.addAttribute("wylosowane", gameService.generateNumber(gameModel));
        model.addAttribute("trafione", gameService.addGoalNumber(gameModel));
        model.addAttribute("saldo", gameService.getSaldo());

        System.out.println(gameService.getSaldo());

        return "game/bet";
    }
}