package kipoderax.virtuallotto.auth.controllers;

import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;
    private UserRepository userRepository;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    @Autowired
    public RegisterController(UserService userService,
                              UserRepository userRepository,
                              StatisticsService statisticsService,
                              HistoryGameDtoService historyGameDtoService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
    }

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("registerForm", new RegisterForm());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterForm registerForm,
                           Model model) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        if (!userService.register(registerForm, model)) {

            return "auth/register";
        }

        return "redirect:/login";
    }
}