package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;


@Controller
public class IndexController {

    private GameService gameService;
    private UserRepository userRepository;

    public IndexController(GameService gameService,
                           UserRepository userRepository) {

        this.gameService = gameService;
        this.userRepository = userRepository;
    }

    @GetMapping({"/"})
    public String showWinners(Model model, GameModel gameModel) {

        Collections.sort(gameModel.getLastNumbers().subList(0, 6));
        model.addAttribute("result", gameModel.getLastNumbers().subList(0, 6));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        return "index";
    }
}
