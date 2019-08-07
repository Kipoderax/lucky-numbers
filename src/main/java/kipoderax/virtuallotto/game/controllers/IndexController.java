package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.game.model.GameModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private MainPageDisplay mainPageDisplay;

    public IndexController(MainPageDisplay mainPageDisplay) {
        this.mainPageDisplay = mainPageDisplay;
    }

    @GetMapping({"/"})
    public String showWinners(Model model, GameModel gameModel) {

        mainPageDisplay.displayGameStatus(model);
        mainPageDisplay.drawNumbers(model, gameModel);
        mainPageDisplay.sumUpLatestPlayersGame(model);

        return "index";
    }

    @GetMapping("/informacje")
    public String information(Model model) {

        mainPageDisplay.displayGameStatus(model);

        return "game/information";
    }
}
