package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.model.GameNoEntity;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BetController {

     private GameService gameService;
     private UserService userService;
     private UserRepository userRepository;

     private UserSession userSession;
     private GameNoEntity games;

    public BetController(GameService gameService, UserService userService, UserSession userSession,
                         GameNoEntity games, UserRepository userRepository) {
        this.gameService = gameService;
        this.userService = userService;
        this.userRepository = userRepository;

        this.userSession = userSession;
        this.games = games;
    }

    @GetMapping("/zaklad/szybkagra")
    public String bet(Model model) {
        GameModel gameModel = new GameModel();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        //todo gameService.getSaldo() <- bedzie pobierać ciągle 100 bo tyle na sztywno ustawiłem.
        // Z repozytorium pobierać właściwą kwotę
        if (games.getSaldo() > 2) {
            model.addAttribute("target", gameService.showTarget());
            model.addAttribute("wylosowane", gameService.generateNumber(gameModel));
            model.addAttribute("trafione", gameService.addGoalNumber(gameModel));
            model.addAttribute("saldo", gameService.getSaldo());
            model.addAttribute("winMoney", gameService.getMyWin());

            userRepository.updateUserSaldoByLogin(
                    gameService.getSaldo(), userSession.getUser().getLogin());
        }
        else {
            model.addAttribute("info", "Brak kasy na kolejny zakład");
        }

            return "game/withoutauth";
//        System.out.println(gameService.getSaldo());
    }


    @GetMapping("/zaklad")
    public String choice() {

        return "game/bet";
    }

//    @PostMapping("/zaklad")
//    public String bet(Model model, @RequestParam int number) {
//
//        return "game/bet";
//    }
}