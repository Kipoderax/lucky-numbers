package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.commons.displays.FormDisplay;
import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.auth.service.UserSession;
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

        formDisplay.numbersForm(model);

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
    public String saveInputNumbers(@ModelAttribute NumbersForm numbersForm, Model model) {
        GameModel gameModel = new GameModel();
        InputNumberValidation inputNumberValidation = new InputNumberValidation();

        if (!inputNumberValidation.isDifferentNumberPairs(gameModel.createNumbersOfNumbersForm(numbersForm))) {

            mainPageDisplay.displayGameStatus(model);

            model.addAttribute("duplicate", duplicateNumbersExist);
            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
            return "game/bet-for-register-users";
        }

        if (!inputNumberValidation.rangeNumbers(gameModel.createNumbersOfNumbersForm(numbersForm))) {

            mainPageDisplay.displayGameStatus(model);

            model.addAttribute("range", awayRangeNumber);
            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
            return "game/bet-for-register-users";
        }



        if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

            userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(numbersForm),
                    userSession.getUser().getId());
        }

        numbersForm.setNumber1(0);
        numbersForm.setNumber2(0);
        numbersForm.setNumber3(0);
        numbersForm.setNumber4(0);
        numbersForm.setNumber5(0);
        numbersForm.setNumber6(0);

        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        mainPageDisplay.displayGameStatus(model);

        return "game/bet-for-register-users";
    }

    @PostMapping("/zaklady-generate")
    public String saveGenerateNumbers(Model model, @ModelAttribute NumbersForm numbersForm) {
        GameModel gameModel = new GameModel();

        if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

            userNumbersService.generateNumber(gameModel, numbersForm);
        }
        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        mainPageDisplay.displayGameStatus(model);

        return "game/bet-for-register-users";
    }

}