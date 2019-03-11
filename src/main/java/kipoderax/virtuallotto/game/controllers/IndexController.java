package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    private UserSession userSession;
    private UserRepository userRepository;

    public IndexController(UserSession userSession,
                           UserRepository userRepository) {

        this.userSession = userSession;

        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(@ModelAttribute LoginForm loginForm, Model model) {
        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        //todo przenieść do AccountControllera i wyświetlać na koncie zalogowanego użytkownika
        model.addAttribute("currentUser", userSession.getUser().getUsername());
        model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));

        return "index";
    }

    @PostMapping("/addMoney")
    public String charge(@RequestParam int chargeSaldo) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        //doładowuje konto
        if (chargeSaldo >= 0 && chargeSaldo <= 200) {

            userRepository.updateUserSaldoByLogin(
                    userRepository.findSaldoByLogin(userSession.getUser().getLogin()) + chargeSaldo,
                    userSession.getUser().getLogin()
            );

            return "redirect:/";
        }

        return "redirect:/";
    }
}