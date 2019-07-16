package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatisticsController {

    private final UserRepository userRepository;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    public StatisticsController(UserRepository userRepository,
                                StatisticsService statisticsService,
                                HistoryGameDtoService historyGameDtoService) {

        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
    }

    @GetMapping("/statystyki")
    public String statistics(Model model) {

        model.addAttribute("userDto", statisticsService.getAllDtoUsersDefault());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());

        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault().subList(0, 5));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        return "game/statistics";
    }

    @PostMapping("/statystyki")
    public String statistics(Model model, @RequestParam("by") String by) {

        model.addAttribute("userDto", statisticsService.getAllDtoUsersBy(by));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());

        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault().subList(0, 5));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        return "game/statistics";
    }

}
