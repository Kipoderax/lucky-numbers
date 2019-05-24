package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.service.UserNumbersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserNumbersController {

    private UserSession userSession;
    private UserNumbersService userNumbersService;
    private UserRepository userRepository;
    private UserBetsRepository userBetsRepository;
    private ApiNumberRepository apiNumberRepository;

    public UserNumbersController(UserNumbersService userNumbersService,
                                 UserSession userSession,
                                 UserRepository userRepository,
                                 UserBetsRepository userBetsRepository,
                                 ApiNumberRepository apiNumberRepository) {

        this.userNumbersService = userNumbersService;
        this.userSession = userSession;
        this.userRepository = userRepository;
        this.userBetsRepository = userBetsRepository;
        this.apiNumberRepository = apiNumberRepository;
    }

    @GetMapping("/mojeliczby")
    public String getUserNumbers(Model model) {

        model.addAttribute("usersavenumbers", userNumbersService.getAllUserNumbersById(userSession.getUser().getId()));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        return "auth/user-numbers";
    }

    @GetMapping("/wyniki")
    public String getUserResults(Model model, GameModel gameModel) {

        //game-result-fragment
            List<Integer> oldList = new ArrayList<>();
            oldList.add(1);
            oldList.add(8);
            oldList.add(11);
            oldList.add(17);
            oldList.add(24);
            oldList.add(31);

            List<Integer> newList = new ArrayList<>();
            newList.add(1);
            newList.add(7);
            newList.add(11);
            newList.add(17);
            newList.add(24);
            newList.add(31);

        if (!userNumbersService.isNewNumberApi(
                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
                gameModel.getLastNumbers())) {
            model.addAttribute("userResult", userNumbersService.checkUserNumbers(userSession.getUser().getId(), gameModel));
            model.addAttribute("amountnumbergame", userBetsRepository.AmountBetsByUserId(userSession.getUser().getId()));

            System.out.println("z bazy danych: " + userNumbersService.getUserApiNumber(userSession.getUser().getId()));
            System.out.println("z api: " + gameModel.getLastNumbers());
        }
        System.out.println("z bazy danych: " + userNumbersService.getUserApiNumber(userSession.getUser().getId()));
        System.out.println("z api: " + gameModel.getLastNumbers());

        return "game/game-result";
    }
}
