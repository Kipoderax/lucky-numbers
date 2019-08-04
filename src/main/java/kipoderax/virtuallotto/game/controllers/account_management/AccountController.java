package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.Experience;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class AccountController {

    private UserSession userSession;

    private UserRepository userRepository;
    private HistoryGameRepository historyGameRepository;

    private UserService userService;
    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    @Value("${game.whileLottery}")
    private String whileLottery;

    public AccountController(UserSession userSession,

                             UserRepository userRepository,
                             HistoryGameRepository historyGameRepository,

                             UserService userService,
                             StatisticsService statisticsService,
                             HistoryGameDtoService historyGameDtoService){

        this.userSession = userSession;

        this.userRepository = userRepository;
        this.historyGameRepository = historyGameRepository;

        this.userService = userService;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {
        Experience experience = new Experience();

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        String username = userSession.getUser().getUsername();
        int userId = userSession.getUser().getId();


        int numberGame;
        if (historyGameRepository.amountBets(username) != null) {
            numberGame = historyGameRepository.amountBets(username);
        } else { numberGame = 0; }

        int three;
        if (historyGameRepository.amountGoalThrees(username) != null) {
            three = historyGameRepository.amountGoalThrees(username);
        } else { three = 0; }

        int four;
        if (historyGameRepository.amountGoalFours(username) != null) {
            four = historyGameRepository.amountGoalFours(username);
        } else { four = 0; }

        int five;
        if (historyGameRepository.amountGoalFives(username) != null) {
            five = historyGameRepository.amountGoalFives(username);
        } else { five = 0; }

        int six;
        if (historyGameRepository.amountGoalSixes(username) != null) {
            six = historyGameRepository.amountGoalSixes(username);
        } else { six = 0; }

        Integer addUp = (three * 24) + (four * 120) + (five * 6000) + (six * 2_000_000);
        if (addUp == null) {
            addUp = 0;
        }

        int userExperience;
        if (historyGameRepository.userExperience(username) != null) {
            userExperience = historyGameRepository.userExperience(username);
        } else { userExperience = 0; }

        userRepository.updateLastLoginByLogin(new Date(), userId);

        model.addAttribute("currentUser", userSession.getUser().getUsername());

        //MAIN INFORMATION CONTENT
        model.addAttribute("username", userRepository.findUsernameByUsername(username));
        model.addAttribute("email", userRepository.findEmailByUserId(userId));
        model.addAttribute("createAccount", userRepository.findDateOfCreateAccountByUserId(username));
        model.addAttribute("lastLogin", userRepository.findLastLoginDateByUserId(username));
        model.addAttribute("saldo", userRepository.findSaldoByUserId(userId));
        model.addAttribute("level", experience.currentLevel(userExperience));
        model.addAttribute("toNextLevel", experience.needExpToNextLevel(userExperience));
        model.addAttribute("onehundred", experience.needExpForAllLevel(userExperience));

        //GAME CONTENT
        model.addAttribute("amountOfThree", three);
        model.addAttribute("amountOfFour", four);
        model.addAttribute("amountOfFive", five);
        model.addAttribute("amountOfSix", six);
        model.addAttribute("numberGame", numberGame);
        model.addAttribute("exp", userExperience);

        //STATISTICS CONTENT
        model.addAttribute("expense", numberGame * 3);
        model.addAttribute("addup", addUp);
        model.addAttribute("result", (addUp - (numberGame * 3)));

        //STATUS CONTENT
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());


        CheckDate checkDate = new CheckDate();
        System.out.println("s"+checkDate.getCurrent()+"e");
        if (checkDate.isLottery()) {

            model.addAttribute("wait", whileLottery);
        }

        return "auth/myaccount";
    }

    @GetMapping("/logout")
    public String logout(Model model) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        userService.logout();

        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        model.addAttribute("passwordForm", new RegisterForm());

        return "auth/change-password";
    }

    @PostMapping("/change-password")
    private String changePassword(@ModelAttribute RegisterForm registerForm, Model model) {


        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        if (userService.changePassword(registerForm)) {

            return "redirect:/";
        }

        return "redirect:/konto";
    }

    @GetMapping("/delete-account")
    public String deleteAccount(Model model) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        model.addAttribute("delete", new RegisterForm());

        return "auth/delete-account";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@ModelAttribute RegisterForm registerForm, Model model) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        if (userService.isCorrectCurrentPassword(registerForm)) {
            userService.deleteAccount(userSession.getUser().getId());

            return "redirect:/";
        }

        return "redirect:/delete-account";
    }

    @GetMapping("/player")
    public String showPlayer(Model model) {
        RegisterForm registerForm = new RegisterForm();

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        model.addAttribute("nick", registerForm);

        return "game/search-player";
    }

    @PostMapping("/player")
    public String showPlayer(Model model, @RequestParam("username") String username) {
        Experience experience = new Experience();
        model.addAttribute("nick", new RegisterForm());
        GameModel gameModel = new GameModel();

        if (userRepository.existsByUsername(username)) {

            int numberGame;
            if (historyGameRepository.amountBets(username) != null) {
                numberGame = historyGameRepository.amountBets(username);
            } else { numberGame = 0; }

            int three;
            if (historyGameRepository.amountGoalThrees(username) != null) {
                three = historyGameRepository.amountGoalThrees(username);
            } else { three = 0; }

            int four;
            if (historyGameRepository.amountGoalFours(username) != null) {
                four = historyGameRepository.amountGoalFours(username);
            } else { four = 0; }

            int five;
            if (historyGameRepository.amountGoalFives(username) != null) {
                five = historyGameRepository.amountGoalFives(username);
            } else { five = 0; }

            int six;
            if (historyGameRepository.amountGoalSixes(username) != null) {
                six = historyGameRepository.amountGoalSixes(username);
            } else { six = 0; }

            Integer addUp = (three * 24) + (four * gameModel.getRewardsMoney()[1])
                    + (five * gameModel.getRewardsMoney()[2]) + (six * gameModel.getRewardsMoney()[3]);
            if (addUp == null) {
                addUp = 0;
            }

            int userExperience;
            if (historyGameRepository.userExperience(username) != null) {
                userExperience = historyGameRepository.userExperience(username);
            } else { userExperience = 0; }

            model.addAttribute("player", userRepository.findUsernameByUsername(username));

            //MAIN INFORMATION CONTENT
            model.addAttribute("username", userRepository.findUsernameByUsername(username));
            model.addAttribute("createAccount", userRepository.findDateOfCreateAccountByUserId(username));
            model.addAttribute("lastLogin", userRepository.findLastLoginDateByUserId(username));
            model.addAttribute("level", experience.currentLevel(userExperience));

            //GAME CONTENT
            model.addAttribute("amountOfThree", three);
            model.addAttribute("amountOfFour", four);
            model.addAttribute("amountOfFive", five);
            model.addAttribute("amountOfSix", six);
            model.addAttribute("numberGame", numberGame);
            model.addAttribute("exp", userExperience);

            //STATISTICS CONTENT
            model.addAttribute("expense", numberGame * 3);
            model.addAttribute("addup", addUp);
            model.addAttribute("result", (addUp - (numberGame * 3)));

            //STATUS CONTENT
            model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
            model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
            model.addAttribute("top5level", statisticsService.get5BestPlayers());
            model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

            return "auth/player-account";
        }

        return "game/search-player";
    }
}