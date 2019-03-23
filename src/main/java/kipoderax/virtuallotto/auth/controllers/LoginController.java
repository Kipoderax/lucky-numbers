package kipoderax.virtuallotto.auth.controllers;

import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(Model model) {

        model.addAttribute("loginForm", new LoginForm());

        return "auth/login";
    }

    @PostMapping("/login")
    public String getLogin(@ModelAttribute LoginForm loginForm,
                           Model model) {

        UserService.LoginResponse loginResponse = userService.login(loginForm);

        if (loginResponse != UserService.LoginResponse.SUCCESS) {
            model.addAttribute("info", loginResponse);

            return "auth/login";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {

        userService.logout();

        return "redirect:/login";
    }
}