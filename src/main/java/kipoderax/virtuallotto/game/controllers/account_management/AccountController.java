package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import kipoderax.virtuallotto.game.service.Experience;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;


@Controller
public class AccountController {

    private UserSession userSession;
    private UserService userService;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserExperienceRepository userExperienceRepository;

    public AccountController(UserSession userSession,
                             UserService userService,
                             UserRepository userRepository,
                             GameRepository gameRepository,
                             UserExperienceRepository userExperienceRepository){

        this.userSession = userSession;
        this.userService = userService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userExperienceRepository = userExperienceRepository;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {
        Experience experience = new Experience();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        int id = userSession.getUser().getId();
        int numberGame = gameRepository.findNumberGameByLogin(id);
        int three = gameRepository.findCountOfThreeByLogin(id);
        int four = gameRepository.findCountOfFourByLogin(id);
        int five = gameRepository.findCountOfFiveByLogin(id);
        int six = gameRepository.findCountOfSixByLogin(id);
        int addUp = (three * 24) + (four * 120) + (five * 6000) + (six * 2_000_000);

        userRepository.updateLastLoginByLogin(new Date(), id);

        model.addAttribute("currentUser", userSession.getUser().getUsername());

        //MAIN INFORMATION CONTENT
        model.addAttribute("email", userRepository.findEmailByLogin(id));
        model.addAttribute("createAccount", userRepository.findDateOfCreateAccountByLogin(id));
        model.addAttribute("lastLogin", userRepository.findLastLoginDateByLogin(id));
        model.addAttribute("saldo", userRepository.findSaldoByLogin(id));
        model.addAttribute("level", userExperienceRepository.findLevelByLogin(id));
        model.addAttribute("toNextLevel", experience.needExpToNextLevel(userExperienceRepository.findLevelByLogin(id),
                userExperienceRepository.findExpByLogin(id)));

        //GAME CONTENT
        model.addAttribute("amountOfThree", three);
        model.addAttribute("amountOfFour", four);
        model.addAttribute("amountOfFive", five);
        model.addAttribute("amountOfSix", six);
        model.addAttribute("numberGame", numberGame);
        model.addAttribute("exp", userExperienceRepository.findExpByLogin(id));

        //STATISTICS CONTENT
        model.addAttribute("expense", numberGame * 3);
        model.addAttribute("addup", addUp);
        model.addAttribute("result", Math.abs(addUp - (numberGame * 3)));

        //STATUS CONTENT
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());


        return "auth/myaccount";
    }

    @GetMapping("/logout")
    public String logout() {

        userService.logout();

        return "redirect:/login";
    }
}