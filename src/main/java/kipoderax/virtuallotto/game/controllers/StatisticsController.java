package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.game.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatisticsController {

    private StatisticsService statisticsService;
    private MainPageDisplay mainPageDisplay;

    public StatisticsController(StatisticsService statisticsService,
                                MainPageDisplay mainPageDisplay) {

        this.statisticsService = statisticsService;
        this.mainPageDisplay = mainPageDisplay;
    }

    @GetMapping("/statystyki")
    public String statistics(Model model) {

        model.addAttribute("userDto", statisticsService.getAllDtoUsersDefault());
        mainPageDisplay.displayGameStatus(model);

        return "game/statistics";
    }

    @PostMapping("/statystyki")
    public String statistics(Model model, @RequestParam("by") String by) {

        model.addAttribute("by", by);
        model.addAttribute("userDto", statisticsService.getAllDtoUsersBy(by));
        mainPageDisplay.displayGameStatus(model);

        return "game/statistics";
    }

}
