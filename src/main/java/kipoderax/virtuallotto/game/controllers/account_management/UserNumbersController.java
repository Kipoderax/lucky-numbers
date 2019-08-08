package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
import kipoderax.virtuallotto.game.service.user_numbers.WinnerBetsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class UserNumbersController {

    private UserSession userSession;
    private MainPageDisplay mainPageDisplay;

    private UserNumbersService userNumbersService;
    private WinnerBetsServiceImpl winnerBetsService;

    private UserBetsRepository userBetsRepository;
    private ApiNumberRepository apiNumberRepository;

    public UserNumbersController(UserNumbersService userNumbersService,
                                 UserSession userSession,
                                 MainPageDisplay mainPageDisplay,
                                 UserBetsRepository userBetsRepository,
                                 ApiNumberRepository apiNumberRepository,
                                 WinnerBetsServiceImpl winnerBetsService) {


        this.userNumbersService = userNumbersService;
        this.userSession = userSession;
        this.mainPageDisplay = mainPageDisplay;
        this.userBetsRepository = userBetsRepository;
        this.apiNumberRepository = apiNumberRepository;
        this.winnerBetsService = winnerBetsService;
    }

    @GetMapping("/mojeliczby")
    public String getUserNumbers(Model model) {

        model.addAttribute("lefts", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        model.addAttribute("usersavenumbers", userNumbersService.getAllUserNumbersById(userSession.getUser().getId()));
        mainPageDisplay.displayGameStatus(model);

        return "auth/user-numbers";
    }

    @PostMapping("/mojeliczby")
    public String QuickAddBets(Model model, @ModelAttribute LottoNumbersDto lottoNumbersDto) {
        GameModel gameModel = new GameModel();
        CheckDate checkDate = new CheckDate();

        if (checkDate.isLottery()) {

            return "redirect:/mojeliczby";

        } else if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

            userNumbersService.generateNumber(gameModel, lottoNumbersDto);
            userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(lottoNumbersDto),
                    userSession.getUser().getId());

        }

        if (!userNumbersService.isNewNumberApi(
                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
                gameModel.getLastNumbers())) {

            return "redirect:/wyniki";
        }

        mainPageDisplay.displayGameStatus(model);
        model.addAttribute("lefts", userNumbersService.leftBetsToSend(userSession.getUser().getId()));

        return "redirect:/mojeliczby";
    }

    @GetMapping("/wyniki")
    public String getUserResults(Model model, GameModel gameModel) {

        mainPageDisplay.displayGameStatus(model);

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
