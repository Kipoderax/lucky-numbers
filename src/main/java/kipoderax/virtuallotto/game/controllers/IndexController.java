package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String showWinners(Model model) {

        model.addAttribute("result", gameService.showTarget());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        return "index";
    }
}
