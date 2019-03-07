package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private UserService userService;
    private UserSession userSession;

    public IndexController(UserSession userSession, UserService userService) {

        this.userSession = userSession;
        this.userService = userService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        model.addAttribute("saldo", userService.userDto().getSaldo());

        return "index";
    }
}