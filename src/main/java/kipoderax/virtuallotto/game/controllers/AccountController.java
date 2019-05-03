package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
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
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserBetsRepository userBetsRepository;

    public AccountController(UserSession userSession,
                             UserRepository userRepository,
                             GameRepository gameRepository,
                             UserBetsRepository userBetsRepository){

        this.userSession = userSession;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userBetsRepository = userBetsRepository;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        model.addAttribute("currentUser", userSession.getUser().getUsername());
        model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));

        model.addAttribute("numberGame", gameRepository.findNumberGameByLogin(userSession.getUser().getLogin()));
        model.addAttribute("amountOfThree", gameRepository.findCountOfThreeByLogin(userSession.getUser().getLogin()));
        model.addAttribute("amountOfFour", gameRepository.findCountOfFourByLogin(userSession.getUser().getLogin()));
        model.addAttribute("amountOfFive", gameRepository.findCountOfFiveByLogin(userSession.getUser().getLogin()));
        model.addAttribute("amountOfSix", gameRepository.findCountOfSixByLogin(userSession.getUser().getLogin()));

        //todo dokonczyc poprzez petle pobranie wszystkich zapisanych obstawien
        for (int i = 3; i < 5; i++) {
            //Optional i warunek do wyciagania
            model.addAttribute("number1", userBetsRepository.findUserNumber1ByLogin(userSession.getUser().getLogin(), i));
            model.addAttribute("number2", userBetsRepository.findUserNumber2ByLogin(userSession.getUser().getLogin(), i));
            model.addAttribute("number3", userBetsRepository.findUserNumber3ByLogin(userSession.getUser().getLogin(), i));
            model.addAttribute("number4", userBetsRepository.findUserNumber4ByLogin(userSession.getUser().getLogin(), i));
            model.addAttribute("number5", userBetsRepository.findUserNumber5ByLogin(userSession.getUser().getLogin(), i));
            model.addAttribute("number6", userBetsRepository.findUserNumber6ByLogin(userSession.getUser().getLogin(), i));
        }

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
}