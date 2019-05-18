package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.service.UserNumbersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserNumbersController {

    private UserSession userSession;
    private UserNumbersService userNumbersService;
    private UserRepository userRepository;

    public UserNumbersController(UserNumbersService userNumbersService,
                                 UserSession userSession,
                                 UserRepository userRepository) {

        this.userNumbersService = userNumbersService;
        this.userSession = userSession;
        this.userRepository = userRepository;
    }

    @GetMapping("/mojeliczby")
    public String getUserNumbers(Model model) {

        model.addAttribute("usersavenumbers", userNumbersService.getAllUserNumbersById(userSession.getUser().getId()));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        return "auth/user-numbers";
    }
}
