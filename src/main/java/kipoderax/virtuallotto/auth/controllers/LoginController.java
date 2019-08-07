package kipoderax.virtuallotto.auth.controllers;

import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.commons.forms.LoginForm;
import kipoderax.virtuallotto.auth.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private UserService userService;
    private MainPageDisplay mainPageDisplay;

    @Value("${error.wrongLogin}")
    private String loginError;

    public LoginController(UserService userService,
                           MainPageDisplay mainPageDisplay) {

        this.userService = userService;
        this.mainPageDisplay = mainPageDisplay;
    }

    @GetMapping("/login")
    public String getLogin(Model model) {

        model.addAttribute("loginForm", new LoginForm());

        mainPageDisplay.displayGameStatus(model);

        return "auth/login";
    }

    @PostMapping("/login")
    public String getLogin(@ModelAttribute LoginForm loginForm,
                           Model model) {

        UserService.Response loginResponse = userService.login(loginForm);

        mainPageDisplay.displayGameStatus(model);

        if (loginResponse != UserService.Response.SUCCESS) {

            model.addAttribute("info", loginError);
            return "auth/login";
        }

        return "redirect:/konto";
    }
}