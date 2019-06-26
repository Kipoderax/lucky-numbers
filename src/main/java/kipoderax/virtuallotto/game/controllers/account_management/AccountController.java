package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.forms.LoginForm;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import kipoderax.virtuallotto.game.service.Experience;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Controller
public class AccountController {

    private UserSession userSession;

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserExperienceRepository userExperienceRepository;

    private UserService userService;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    public AccountController(UserSession userSession,

                             UserRepository userRepository,
                             GameRepository gameRepository,
                             UserExperienceRepository userExperienceRepository,

                             UserService userService,
                             StatisticsService statisticsService,
                             HistoryGameDtoService historyGameDtoService){

        this.userSession = userSession;

        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userExperienceRepository = userExperienceRepository;

        this.userService = userService;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
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
        model.addAttribute("username", userRepository.findUsernameByUserId(id));
        model.addAttribute("email", userRepository.findEmailByUserId(id));
        model.addAttribute("createAccount", userRepository.findDateOfCreateAccountByUserId(id));
        model.addAttribute("lastLogin", userRepository.findLastLoginDateByUserId(id));
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
        model.addAttribute("result", (addUp - (numberGame * 3)));

        //STATUS CONTENT
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault().subList(0, 5));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());


        return "auth/myaccount";
    }

    @GetMapping("/logout")
    public String logout() {

        userService.logout();

        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {

        model.addAttribute("passwordForm", new RegisterForm());

        return "auth/change-password";
    }

    @PostMapping("/change-password")
    private String changePassword(Model model, @ModelAttribute RegisterForm registerForm) {


        if (userService.changePassword(registerForm)) {

            return "redirect:/";
        }

        return "redirect:/konto";
    }
}