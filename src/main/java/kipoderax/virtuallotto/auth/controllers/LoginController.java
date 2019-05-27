package kipoderax.virtuallotto.auth.controllers;

import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.commons.dtos.models.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UserService userService;
    private UserRepository userRepository;

    @Autowired
    public LoginController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        UserDto userDto = new UserDto();

        model.addAttribute("loginForm", new LoginForm());
        System.out.println(userDto.getUsername());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

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

        return "redirect:/konto";
    }
}