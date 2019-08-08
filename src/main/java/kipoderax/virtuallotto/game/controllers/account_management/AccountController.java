package kipoderax.virtuallotto.game.controllers.account_management;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.displays.AccountDisplay;
import kipoderax.virtuallotto.commons.displays.FormDisplay;
import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.commons.forms.HistoryGameForm;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.commons.validation.CheckDate;
import kipoderax.virtuallotto.game.service.AccountService;
import kipoderax.virtuallotto.game.service.Experience;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class AccountController {

    private UserSession userSession;
    private MainPageDisplay mainPageDisplay;
    private FormDisplay formDisplay;
    private AccountDisplay accountDisplay;

    private UserRepository userRepository;

    private UserService userService;
    private AccountService accountService;

    private HistoryGameForm historyGameForm = new HistoryGameForm();
    private Experience experience = new Experience();

    @Value("${game.whileLottery}")
    private String whileLottery;
    @Value("${error.usernameNotExist}")
    private String usernameNotExist;

    public AccountController(UserSession userSession,
                             MainPageDisplay mainPageDisplay,
                             FormDisplay formDisplay,
                             AccountDisplay accountDisplay,

                             UserRepository userRepository,

                             UserService userService,
                             AccountService accountService){

        this.userSession = userSession;
        this.mainPageDisplay = mainPageDisplay;
        this.formDisplay = formDisplay;
        this.accountDisplay = accountDisplay;

        this.userRepository = userRepository;

        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping({"/konto"})
    public String index(Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        String username = userSession.getUser().getUsername();
        int userId = userSession.getUser().getId();
        model.addAttribute("currentUser", userSession.getUser().getUsername());

        accountService.setDefaultValues(historyGameForm, username);
        userRepository.updateLastLoginByLogin(new Date(), userId);

        accountDisplay.mainInformation(model, username, userId, experience, historyGameForm);
        accountDisplay.amountGoal(model, historyGameForm);
        accountDisplay.statisticsInformation(model, username,historyGameForm);
        mainPageDisplay.displayGameStatus(model);

        CheckDate checkDate = new CheckDate();
        if (checkDate.isLottery()) {

            model.addAttribute("wait", whileLottery);
        }

        return "auth/myaccount";
    }

    @GetMapping("/logout")
    public String logout() {

        userService.logout();

        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {

        mainPageDisplay.displayGameStatus(model);
        formDisplay.registerForm(model);

        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute RegisterForm registerForm) {

        if (userService.changePassword(registerForm)) {

            return "redirect:/konto";
        }

        return "redirect:/change-password";
    }

    @GetMapping("/delete-account")
    public String deleteAccount(Model model) {

        if (!userSession.isUserLogin()) {

            return "redirect:/login";
        }

        mainPageDisplay.displayGameStatus(model);
        formDisplay.registerForm(model);

        return "auth/delete-account";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@ModelAttribute RegisterForm registerForm) {

        if (userService.isCorrectCurrentPassword(registerForm)) {
            userService.deleteAccount(userSession.getUser().getId());

            return "redirect:/";
        }

        return "redirect:/delete-account";
    }

    @GetMapping("/player")
    public String showPlayer(Model model) {

        mainPageDisplay.displayGameStatus(model);
        formDisplay.registerForm(model);

        return "game/search-player";
    }

    @PostMapping("/player")
    public String showPlayer(Model model, @RequestParam("username") String username) {
        formDisplay.registerForm(model);

        if (userRepository.existsByUsername(username)) {

            accountService.setDefaultValues(historyGameForm, username);

            accountDisplay.mainInformation(model, username, 1, experience, historyGameForm);
            accountDisplay.amountGoal(model, historyGameForm);
            mainPageDisplay.displayGameStatus(model);

            accountDisplay.statisticsInformation(model, username, historyGameForm);

            return "auth/player-account";
        } else {
            model.addAttribute("usernameNotExist", usernameNotExist);
            mainPageDisplay.displayGameStatus(model);

            return "game/search-player";
        }
    }

}