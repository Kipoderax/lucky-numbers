package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;


@Controller
public class IndexController {

    private UserRepository userRepository;
    private UserNumbersService userNumbersService;

    public IndexController(UserRepository userRepository,
                           UserNumbersService userNumbersService) {

        this.userRepository = userRepository;
        this.userNumbersService = userNumbersService;
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
