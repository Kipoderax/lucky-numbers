package kipoderax.virtuallotto.auth.controllers;

import kipoderax.virtuallotto.commons.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UserService userService;
    private UserRepository userRepository;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    @Autowired
    public LoginController(UserService userService,
                           UserRepository userRepository,
                           StatisticsService statisticsService,
                           HistoryGameDtoService historyGameDtoService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
    }

    @GetMapping("/login")
    public String getLogin(Model model) {

        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault(PageRequest.of(0, 5)));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        return "auth/login";
    }

    @PostMapping("/login")
    public String getLogin(@ModelAttribute LoginForm loginForm,
                           Model model) {

        UserService.Response loginResponse = userService.login(loginForm);

        if (loginResponse != UserService.Response.SUCCESS) {
            model.addAttribute("info", loginResponse);

            return "auth/login";
        }

        return "redirect:/konto";
    }
}