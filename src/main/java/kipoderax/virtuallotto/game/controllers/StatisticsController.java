package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public StatisticsController(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/statystyki")
    public String statistics(Model model) {

//        model.addAttribute("name", userRepository.getAllLogin());

        return "game/statistics";
    }
}
