package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import kipoderax.virtuallotto.game.service.Experience;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


@Controller
public class AccountController {

    private UserSession userSession;
    private UserService userService;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserBetsRepository userBetsRepository;
    private UserExperienceRepository userExperienceRepository;

    public AccountController(UserSession userSession,
                             UserService userService,
                             UserRepository userRepository,
                             GameRepository gameRepository,
                             UserBetsRepository userBetsRepository,
                             UserExperienceRepository userExperienceRepository){

        this.userSession = userSession;
        this.userService = userService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userBetsRepository = userBetsRepository;
        this.userExperienceRepository = userExperienceRepository;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {
        Experience experience = new Experience();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        String login = userSession.getUser().getLogin();
        int numberGame = gameRepository.findNumberGameByLogin(login);
        int three = gameRepository.findCountOfThreeByLogin(login);
        int four = gameRepository.findCountOfFourByLogin(login);
        int five = gameRepository.findCountOfFiveByLogin(login);
        int six = gameRepository.findCountOfSixByLogin(login);
        int addUp = (three * 24) + (four * 120) + (five * 6000) + (six * 2_000_000);

        userRepository.updateLastLoginByLogin(new Date(), userSession.getUser().getLogin());

        model.addAttribute("currentUser", userSession.getUser().getUsername());

        //MAIN INFORMATION CONTENT
        model.addAttribute("email", userRepository.findEmailByLogin(login));
        model.addAttribute("createAccount", userRepository.findDateOfCreateAccountByLogin(login));
        model.addAttribute("lastLogin", userRepository.findLastLoginDateByLogin(login));
        model.addAttribute("saldo", userRepository.findSaldoByLogin(login));
        model.addAttribute("level", userExperienceRepository.findLevelByLogin(login));
        model.addAttribute("toNextLevel", experience.needExpToNextLevel(userExperienceRepository.findLevelByLogin(login),
                userExperienceRepository.findExpByLogin(login)));

        //GAME CONTENT
        model.addAttribute("amountOfThree", three);
        model.addAttribute("amountOfFour", four);
        model.addAttribute("amountOfFive", five);
        model.addAttribute("amountOfSix", six);
        model.addAttribute("numberGame", numberGame);
        model.addAttribute("exp", userExperienceRepository.findExpByLogin(login));

        //STATISTICS CONTENT
        model.addAttribute("expense", numberGame * 3);
        model.addAttribute("addup", addUp);
        model.addAttribute("result", Math.abs(addUp - (numberGame * 3)));

        //STATUS CONTENT
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());


        return "auth/myaccount";
    }

    @PostMapping("/addMoney")
    public String charge(@RequestParam int chargeSaldo) {

        int level = userExperienceRepository.findLevelByLogin(userSession.getUser().getLogin());

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        //doładowuje konto
        if (chargeSaldo >= 0 && chargeSaldo <= (50 + (level * 2))) {

            userRepository.updateUserSaldoByLogin(
                    userRepository.findSaldoByLogin(userSession.getUser().getLogin()) + chargeSaldo,
                    userSession.getUser().getLogin()
            );

            return "redirect:/konto";
        }

        return "redirect:/konto";
    }

    @GetMapping("/logout")
    public String logout() {

        userService.logout();

        return "redirect:/login";
    }
}