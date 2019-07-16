package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.GameService;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BetController {

     private GameService gameService;
     private UserSession userSession;
     private UserRepository userRepository;
     private UserNumbersService userNumbersService;

     @Value("${game.rangeNumbers}")
     private String awayRangeNumber;

     @Value("${game.duplicateNumbers}")
     private String duplicateNumbersExist;

    public BetController(GameService gameService,
                         UserSession userSession,
                         UserRepository userRepository,
                         UserNumbersService userNumbersService) {

        this.gameService = gameService;
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.userNumbersService = userNumbersService;
    }

    @GetMapping("/zaklady")
    public String saveInputNumbers(Model model, GameModel gameModel) {

        if (userSession.getUser() == null) {

            return "auth/login";
        }

        model.addAttribute("numbersForm", new NumbersForm());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());

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

            model.addAttribute("duplicate", duplicateNumbersExist);
            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
            return "game/bet-for-register-users";
        }

        if (!inputNumberValidation.rangeNumbers(gameModel.createNumbersOfNumbersForm(numbersForm))) {

            model.addAttribute("range", awayRangeNumber);
            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
            return "game/bet-for-register-users";
        }

        userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(numbersForm),
                userSession.getUser().getId());

            model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());


        return "game/bet-for-register-users";
    }

    @PostMapping("/zaklady")
    public String saveGenerateNumbers(Model model, @ModelAttribute NumbersForm numbersForm) {
        GameModel gameModel = new GameModel();

        if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

            gameService.generateNumber(gameModel, numbersForm);
            userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(numbersForm),
                    userSession.getUser().getId());
        }
        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("amountOnline", userRepository.findAllActiveUsers());


        return "game/bet-for-register-users";
    }

}