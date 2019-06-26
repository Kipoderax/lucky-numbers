package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import kipoderax.virtuallotto.game.service.Experience;
import kipoderax.virtuallotto.game.service.GameService;
import kipoderax.virtuallotto.game.service.user_numbers.UserNumbersService;
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
     private UserExperienceRepository userExperienceRepository;
     private UserNumbersService userNumbersService;

    public BetController(GameService gameService,
                         UserSession userSession,
                         UserRepository userRepository,
                         UserExperienceRepository userExperienceRepository,
                         UserNumbersService userNumbersService) {

        this.gameService = gameService;
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.userExperienceRepository = userExperienceRepository;
        this.userNumbersService = userNumbersService;
    }

    @GetMapping("/zaklad")
    public String bet(Model model) {
        GameModel gameModel = new GameModel();
        NumbersForm numbersForm = new NumbersForm();
        Experience experience = new Experience();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";

        }
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        model.addAttribute("numbersForm", new NumbersForm());

        //Pobierz saldo z bazy danych zalogowanego użytkownika
        gameModel.setSaldo(
                userRepository.findSaldoByLogin(
                        userSession.getUser().getId()));
        gameModel.setExperience(
                userExperienceRepository.findExpByLogin(
                        userSession.getUser().getId()));
        gameModel.setLevel(
                userExperienceRepository.findExpByLogin(
                        userSession.getUser().getId()));

        if (gameModel.getSaldo() > 0) {

            model.addAttribute("target", gameService.showTarget());
            model.addAttribute("wylosowane", gameService.generateNumber(gameModel, numbersForm));
            model.addAttribute("trafione", gameService.addGoalNumber(gameModel));

            //Aktualizuj po ilości trafionych liczb
            userRepository.updateUserSaldoByLogin(
                    gameService.getSaldo(gameModel), userSession.getUser().getId());
            userExperienceRepository.updateExperienceById(userSession.getUser().getId(), gameModel.getExperience());
            userExperienceRepository.updateLevelById(userSession.getUser().getId(), experience.reachNextLevel(gameModel.getExperience()));


            model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getId()));
            model.addAttribute("winMoney", gameService.getMyWin(gameModel));

        }
        else {
            model.addAttribute("info", "Brak kasy na kolejny zakład");

        }

            return "game/bet";
    }

    @GetMapping("/zaklady")
    public String saveInputNumbers(Model model) {

        if (userSession.getUser() == null) {
            model.addAttribute("info", "Musisz być zalogowany");

            return "auth/login";
        }
        model.addAttribute("numbersForm", new NumbersForm());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

//        model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getId()) / 3);
        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));

        return "game/bet-for-register-users";
    }

    @PostMapping("/zakladyinput")
    public String saveInputNumbers(@ModelAttribute NumbersForm numbersForm) {
        GameModel gameModel = new GameModel();
        InputNumberValidation inputNumberValidation = new InputNumberValidation();
//
//        if (!userNumbersService.isNewNumberApi(
//                userNumbersService.getUserApiNumber(userSession.getUser().getId()),
//                gameModel.getLastNumbers())) {

            if (inputNumberValidation.isDifferentNumberPairs(gameModel.createNumbersOfNumbersForm(numbersForm))) {

                if (inputNumberValidation.rangeNumbers(gameModel.createNumbersOfNumbersForm(numbersForm))) {

                    if (userNumbersService.leftBetsToSend(userSession.getUser().getId()) != 0) {

                        userNumbersService.saveUserInputNumbers(gameModel.createNumbersOfNumbersForm(numbersForm),
                                userSession.getUser().getId());
                    }
                }
            }
//        }



        return "redirect:/zaklady";
    }

    @PostMapping("/zaklady")
    public String saveGenerateNumbers(Model model, @ModelAttribute NumbersForm numbersForm) {
        GameModel gameModel = new GameModel();

        gameService.generateNumber(gameModel, numbersForm);
        model.addAttribute("saldo", userNumbersService.leftBetsToSend(userSession.getUser().getId()));

        return "game/bet-for-register-users";
    }
}