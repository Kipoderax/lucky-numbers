package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class IndexController {

    private UserService userService;
    private UserSession userSession;
    private UserRepository userRepository;

    public IndexController(UserSession userSession, UserService userService,
                           UserRepository userRepository) {

        this.userSession = userSession;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model, @ModelAttribute LoginForm loginForm, User userDto) {

        if (!userSession.isUserLogin()) {

            model.addAttribute("currentUser", userService.getuserDto("Necroid"));
            System.out.println(userService.getuserDto(userDto.getLogin()));
            return "redirect:/login";
        }

        model.addAttribute("currentUser", userSession.getUser().getLogin());
        System.out.println(userSession.getUser().getSaldo());
        model.addAttribute("saldo", userSession.getUser().getSaldo());
//        model.addAttribute("saldo", saldo1);

//        model.addAttribute("saldo", userService.getSaldo(userDto.getSaldo(), loginForm));

        return "index";
    }
}