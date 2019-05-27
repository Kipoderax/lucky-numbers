package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.game.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class StatisticsController {

    private final UserRepository userRepository;
    private StatisticsService statisticsService;


    public StatisticsController(UserRepository userRepository,
                                StatisticsService statisticsService) {

        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statystyki")
    public String statistics(Model model) {

        model.addAttribute("userDto", statisticsService.getAllDtoUsersDefault());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        System.out.println("max id: " + userRepository.findMaxId());

        for (int i = 1; i <= userRepository.findMaxId(); i++) {
            Optional<User> optionalUser = userRepository.findById(i);

            if (!optionalUser.isPresent()) {
                continue;
            } else {
                System.out.println("Wyswietl saldo: " + userRepository.findSaldoByLogin(i));
            }
        }

        return "game/statistics";
    }

    @PostMapping("/statystyki")
    public String statistics(Model model, @RequestParam("by") String by) {

        model.addAttribute("userDto", statisticsService.getAllDtoUsersBy(by));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        return "game/statistics";
    }
}
