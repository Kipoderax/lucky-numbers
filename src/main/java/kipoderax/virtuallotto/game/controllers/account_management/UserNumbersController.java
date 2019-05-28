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

        if (!userNumbersService.isNewNumberApi(
                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
                gameModel.getLastNumbers())) {
            model.addAttribute("userResult", userNumbersService.checkUserNumbers(gameModel, userSession.getUser().getId()));
            model.addAttribute("amountnumbergame", userBetsRepository.AmountBetsByUserId(userSession.getUser().getId()));

            System.out.println("z bazy danych: " + userNumbersService.getUserApiNumber(userSession.getUser().getId()));
            System.out.println("z api: " + gameModel.getLastNumbers().subList(0, 6));

            apiNumberRepository.updateApiNumbers(userSession.getUser().getId(),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(0),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(1),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(2),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(3),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(4),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(5));

            userBetsRepository.deleteUserBetsAfterShowResult(userSession.getUser().getId());
        }

        return "game/game-result";
    }
}
