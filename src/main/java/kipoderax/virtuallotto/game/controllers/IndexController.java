package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class IndexController {

    private UserRepository userRepository;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    public IndexController(UserRepository userRepository,
                           StatisticsService statisticsService,
                           HistoryGameDtoService historyGameDtoService) {

        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
    }

    @GetMapping({"/"})
    public String showWinners(Model model, GameModel gameModel) {

        Collections.sort(gameModel.getLastNumbers().subList(0, 6));
        model.addAttribute("result", gameModel.getLastNumbers().subList(0, 6));
        model.addAttribute("fourth", gameModel.getRewardsMoney()[2]);
        model.addAttribute("fifth", gameModel.getRewardsMoney()[3]);
        model.addAttribute("sixth", gameModel.getRewardsMoney()[4]);

        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault().subList(0, 5));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        return "index";
    }
}
