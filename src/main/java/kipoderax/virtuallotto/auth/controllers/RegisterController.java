package kipoderax.virtuallotto.auth.controllers;

import kipoderax.virtuallotto.auth.forms.RegisterForm;
import kipoderax.virtuallotto.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("registerForm", new RegisterForm());

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterForm registerForm,
                           Model model) {

        if (!userService.register(registerForm)) {
            model.addAttribute("info", "Login is already use");

            return "auth/register";
        }

        return "redirect:/login";
    }
}