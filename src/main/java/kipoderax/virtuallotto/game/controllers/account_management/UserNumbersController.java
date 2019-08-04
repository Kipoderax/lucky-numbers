package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.service.GameService;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
import kipoderax.virtuallotto.game.service.user_numbers.WinnerBetsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class UserNumbersController {

    private UserSession userSession;

    private UserNumbersService userNumbersService;
    private WinnerBetsServiceImpl winnerBetsService;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;
    private GameService gameService;

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
                                 WinnerBetsServiceImpl winnerBetsService,
                                 GameService gameService) {


        this.userNumbersService = userNumbersService;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
        this.userSession = userSession;
        this.userRepository = userRepository;
        this.userBetsRepository = userBetsRepository;
        this.apiNumberRepository = apiNumberRepository;
        this.gameService = gameService;
        this.winnerBetsService = winnerBetsService;
    }

    @GetMapping("/mojeliczby")
    public String getUserNumbers(Model model) {

        model.addAttribute("lefts", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        model.addAttribute("usersavenumbers", userNumbersService.getAllUserNumbersById(userSession.getUser().getId()));

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        return "auth/user-numbers";
    }

    @PostMapping("/mojeliczby")
    public String QuickAddBets(Model model, @ModelAttribute NumbersForm numbersForm) {
        GameModel gameModel = new GameModel();
        if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

            gameService.generateNumber(gameModel, numbersForm);
            userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(numbersForm),
                    userSession.getUser().getId());
        }

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("lefts", userNumbersService.leftBetsToSend(userSession.getUser().getId()));

        return "redirect:/mojeliczby";
    }

    @GetMapping("/wyniki")
    public String getUserResults(Model model, GameModel gameModel) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        CheckDate checkDate = new CheckDate();
        if ( checkDate.isLottery() ) {

            return "redirect:/konto";
        }

        if (userNumbersService.isNewNumberApi(
                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
                gameModel.getLastNumbers())) {

            return "redirect:/konto";
        } else {

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
