package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.GameRepository;
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

    public AccountController(UserSession userSession,
                             UserRepository userRepository,
                             GameRepository gameRepository){
        this.userSession = userSession;
        this.userRepository = userRepository;

        this.gameRepository = gameRepository;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {
        GameModel gameModel = new GameModel();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        gameModel.setNumberGame(gameRepository.findNumberGame());

        model.addAttribute("currentUser", userSession.getUser().getUsername());
        model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));

        model.addAttribute("numberGame", gameRepository.findNumberGame());
        model.addAttribute("amountOfThree", gameRepository.findCountOfThree());
        model.addAttribute("amountOfFour", gameRepository.findCountOfFour());
        model.addAttribute("amountOfFive", gameRepository.findCountOfFive());
        model.addAttribute("amountOfSix", gameRepository.findCountOfSix());

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