package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private UserService userService;
    private GameService gameService;

    private UserSession userSession;
    private UserRepository userRepository;

    public IndexController(UserSession userSession, UserService userService,
                           GameService gameService, UserRepository userRepository) {

        this.userSession = userSession;
        this.userService = userService;
        this.gameService = gameService;

        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(@ModelAttribute LoginForm loginForm, Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        model.addAttribute("currentUser", userSession.getUser().getUsername());
        model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));

        return "index";
    }

//    @GetMapping("/logout")
//    public String logout() {
//        userService.logout();
//
//        return "redirect:/login";
//    }

    @PostMapping("/")
    public String charge(@RequestParam int saldo, Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        gameService.chargeSaldo(saldo);
        model.addAttribute("charge", userSession.getUser().getSaldo());

        return "index";
    }
}