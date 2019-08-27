package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.commons.displays.FormDisplay;
import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BetController {

     private UserSession userSession;
     private MainPageDisplay mainPageDisplay;
     private FormDisplay formDisplay;
     private UserNumbersService userNumbersService;

     @Value("${game.rangeNumbers}")
     private String awayRangeNumber;

     @Value("${game.duplicateNumbers}")
     private String duplicateNumbersExist;

    public BetController(UserSession userSession,
                         MainPageDisplay mainPageDisplay,
                         FormDisplay formDisplay,
                         UserNumbersService userNumbersService) {

        this.userSession = userSession;
        this.mainPageDisplay = mainPageDisplay;
        this.formDisplay = formDisplay;
        this.userNumbersService = userNumbersService;
    }

    @GetMapping("/zaklady")
    public String saveInputNumbers(Model model, GameModel gameModel) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        formDisplay.numbersForm(model, new LottoNumbersDto());
        mainPageDisplay.displayGameStatus(model);

        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));

        CheckDate checkDate = new CheckDate();
        if (checkDate.isLottery()) {

            return "redirect:/konto";
        }

        if (!userNumbersService.isNewNumberApi(
                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
                gameModel.getLastNumbers())) {

            return "redirect:/wyniki";
        }

        return "game/bet-for-register-users";
    }

    @PostMapping("/zakladyinput")
    public String saveInputNumbers(@ModelAttribute LottoNumbersDto lottoNumbersDto, Model model) {
        GameModel gameModel = new GameModel();
        InputNumberValidation inputNumberValidation = new InputNumberValidation();

        if (!inputNumberValidation.isDifferentNumberPairs(gameModel.createNumbersOfNumbersForm(lottoNumbersDto))) {

            mainPageDisplay.displayGameStatus(model);
            formDisplay.numbersForm(model, lottoNumbersDto);

            model.addAttribute("duplicate", duplicateNumbersExist);
            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
            return "game/bet-for-register-users";
        }

        if (!inputNumberValidation.rangeNumbers(gameModel.createNumbersOfNumbersForm(lottoNumbersDto))) {

            mainPageDisplay.displayGameStatus(model);
            formDisplay.numbersForm(model, lottoNumbersDto);

            model.addAttribute("range", awayRangeNumber);
            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
            return "game/bet-for-register-users";
        }

        if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

            userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(lottoNumbersDto),
                    userSession.getUser().getId());
            return "redirect:/zaklady";
        }

        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        mainPageDisplay.displayGameStatus(model);

        return "game/bet-for-register-users";
    }

    @GetMapping("/zaklady-generate")
    public String saveGenerateNumbers(Model model, @ModelAttribute LottoNumbersDto lottoNumbersDto) {

        userNumbersService.generateNumber(lottoNumbersDto);
        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        mainPageDisplay.displayGameStatus(model);
        formDisplay.numbersForm(model, lottoNumbersDto);

        return "game/bet-for-register-users";
    }

}