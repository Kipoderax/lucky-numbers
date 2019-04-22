package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private GameService gameService;

    public IndexController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String showWinners(Model model) {

        model.addAttribute("result", gameService.showTarget());
//        model.addAttribute("wins", gameService.showWins());

        return "index";
    }
}
