package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
import kipoderax.virtuallotto.game.service.user_numbers.WinnerBetsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class UserNumbersController {

    private UserSession userSession;

    private UserNumbersService userNumbersService;
    private WinnerBetsServiceImpl winnerBetsService;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    private UserRepository userRepository;
    private UserBetsRepository userBetsRepository;
    private ApiNumberRepository apiNumberRepository;


    @Autowired
    public UserNumbersController(UserNumbersService userNumbersService,
                                 StatisticsService statisticsService,
                                 HistoryGameDtoService historyGameDtoService,
                                 UserSession userSession,
                                 UserRepository userRepository,
                                 UserBetsRepository userBetsRepository,
                                 ApiNumberRepository apiNumberRepository,
                                 WinnerBetsServiceImpl winnerBetsService) {


        this.userNumbersService = userNumbersService;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
        this.userSession = userSession;
        this.userRepository = userRepository;
        this.userBetsRepository = userBetsRepository;
        this.apiNumberRepository = apiNumberRepository;

        this.winnerBetsService = winnerBetsService;
    }

    @GetMapping("/mojeliczby")
    public String getUserNumbers(Model model) {

        model.addAttribute("usersavenumbers", userNumbersService.getAllUserNumbersById(userSession.getUser().getId()));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());
        return "auth/user-numbers";
    }

    @GetMapping("/wyniki")
    public String getUserResults(Model model, GameModel gameModel) {

        model.addAttribute("top5level", statisticsService.getAllDtoUsersDefault().subList(0, 5));
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());

        CheckDate checkDate = new CheckDate();
        if ( checkDate.isLottery() ) {

            return "redirect:/konto";
        }

        if (!userNumbersService.isNewNumberApi(
                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
                gameModel.getLastNumbers())) {

            model.addAttribute("userResult", userNumbersService.checkUserNumbers(gameModel, userSession.getUser().getId(),
                    userSession.getUser().getUsername()));
            model.addAttribute("amountnumbergame", userBetsRepository.AmountBetsByUserId(userSession.getUser().getId()));

            apiNumberRepository.updateApiNumbers(userSession.getUser().getId(),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(0),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(1),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(2),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(3),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(4),
                    gameModel.getConvertToJson().getLastLottoNumbers().get(5));

            model.addAttribute("with3numbers", winnerBetsService.getWinnerBetsWith3Numbers());
            model.addAttribute("with4numbers", winnerBetsService.getWinnerBetsWith4Numbers());
            model.addAttribute("with5numbers", winnerBetsService.getWinnerBetsWith5Numbers());
            model.addAttribute("with6numbers", winnerBetsService.getWinnerBetsWith6Numbers());

            Collections.sort(gameModel.getLastNumbers().subList(0, 6));
            model.addAttribute("newapinumbers", gameModel.getLastNumbers().subList(0, 6));

            userBetsRepository.deleteUserBetsAfterShowResult(userSession.getUser().getId());
        }

        return "game/game-result";
    }
}
