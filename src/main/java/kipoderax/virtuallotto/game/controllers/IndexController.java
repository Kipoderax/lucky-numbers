package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.dtos.models.UserDto;
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
    public String index(Model model, UserDto user) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        model.addAttribute("saldo", userService.getSaldo());

        return "index";
    }
}