package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AccountController {

    private UserSession userSession;
    private UserService userService;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserBetsRepository userBetsRepository;

    public AccountController(UserSession userSession,
                             UserService userService,
                             UserRepository userRepository,
                             GameRepository gameRepository,
                             UserBetsRepository userBetsRepository){

        this.userSession = userSession;
        this.userService = userService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userBetsRepository = userBetsRepository;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        int numberGame = gameRepository.findNumberGameByLogin(userSession.getUser().getLogin());
        int three = gameRepository.findCountOfThreeByLogin(userSession.getUser().getLogin());
        int four = gameRepository.findCountOfFourByLogin(userSession.getUser().getLogin());
        int five = gameRepository.findCountOfFiveByLogin(userSession.getUser().getLogin());
        int six = gameRepository.findCountOfSixByLogin(userSession.getUser().getLogin());
        int addUp = (three * 24) + (four * 120) + (five * 6000) + (six * 2_000_000);


        model.addAttribute("currentUser", userSession.getUser().getUsername());
        model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));

        model.addAttribute("numberGame", numberGame);
        model.addAttribute("amountOfThree", three);
        model.addAttribute("amountOfFour", four);
        model.addAttribute("amountOfFive", five);
        model.addAttribute("amountOfSix", six);

        model.addAttribute("expense", numberGame * 3);
        model.addAttribute("addup", addUp);
        model.addAttribute("result", Math.abs(addUp - (numberGame * 3)));

        return "auth/myaccount";
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