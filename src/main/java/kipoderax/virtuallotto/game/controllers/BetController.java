package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.forms.NumbersForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
//@CrossOrigin(origins = "http://localhost:4200")
public class BetController {

     private GameService gameService;
     private UserSession userSession;
     private UserRepository userRepository;
     private UserBetsRepository userBetsRepository;

    public BetController(GameService gameService,
                         UserSession userSession,
                         UserRepository userRepository,
                         UserBetsRepository userBetsRepository) {

        this.gameService = gameService;
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.userBetsRepository = userBetsRepository;
    }

    @GetMapping("/zaklad")
    public String bet(Model model) {
        GameModel gameModel = new GameModel();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";

        }

        //Pobierz saldo z bazy danych zalogowanego użytkownika
        gameModel.setSaldo(
                userRepository.findSaldoByLogin(
                        userSession.getUser().getLogin()));

        if (gameModel.getSaldo() > 0) {

            model.addAttribute("target", gameService.showTarget());
            model.addAttribute("wylosowane", gameService.generateNumber(gameModel));
            model.addAttribute("trafione", gameService.addGoalNumber(gameModel));

            //Aktualizuj po ilości trafionych liczb
            userRepository.updateUserSaldoByLogin(
                    gameService.getSaldo(gameModel), userSession.getUser().getLogin());

            model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));
            model.addAttribute("winMoney", gameService.getMyWin(gameModel));

        }
        else {
            model.addAttribute("info", "Brak kasy na kolejny zakład");

        }

            return "game/bet";
    }

    @GetMapping("/saveNumber")
    public String saveInputNumbers(Model model) {

        model.addAttribute("numbersForm", new NumbersForm());

        return "game/bet";
    }

    @PostMapping("/zaklad")
    public String saveInputNumbers(@ModelAttribute NumbersForm numbersForm, Model model) {

        gameService.saveUserInputNumbers(numbersForm);

        return "redirect:/zaklad";
    }
}