package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.forms.NumbersForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import kipoderax.virtuallotto.game.service.Experience;
import kipoderax.virtuallotto.game.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
//@CrossOrigin(origins = "http://localhost:4200")
public class BetController {

     private GameService gameService;
     private UserSession userSession;
     private UserRepository userRepository;
     private UserExperienceRepository userExperienceRepository;

    public BetController(GameService gameService,
                         UserSession userSession,
                         UserRepository userRepository,
                         UserExperienceRepository userExperienceRepository) {

        this.gameService = gameService;
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.userExperienceRepository = userExperienceRepository;
    }

    @GetMapping("/zaklad")
    public String bet(Model model) {
        GameModel gameModel = new GameModel();
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
                        userSession.getUser().getLogin()));
        gameModel.setExperience(
                userExperienceRepository.findExpByLogin(
                        userSession.getUser().getLogin()));
        gameModel.setLevel(
                userExperienceRepository.findExpByLogin(
                        userSession.getUser().getLogin()));

        if (gameModel.getSaldo() > 0) {

            model.addAttribute("target", gameService.showTarget());
            model.addAttribute("wylosowane", gameService.generateNumber(gameModel));
            model.addAttribute("trafione", gameService.addGoalNumber(gameModel));

            //Aktualizuj po ilości trafionych liczb
            userRepository.updateUserSaldoByLogin(
                    gameService.getSaldo(gameModel), userSession.getUser().getLogin());
            userExperienceRepository.updateExperienceById(userSession.getUser().getId(), gameModel.getExperience());
            userExperienceRepository.updateLevelById(userSession.getUser().getId(), experience.reachNextLevel(gameModel.getExperience()));


            model.addAttribute("saldo", userRepository.findSaldoByLogin(userSession.getUser().getLogin()));
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

        return "game/bet-for-register-users";
    }

    @PostMapping("/zaklady")
    public String saveInputNumbers(@ModelAttribute NumbersForm numbersForm) {

        gameService.saveUserInputNumbers(numbersForm, userSession.getUser().getId());

        return "redirect:/zaklady";
    }
}