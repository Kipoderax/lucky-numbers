package kipoderax.virtuallotto.commons.mail;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.repositories.UserTokenRepository;
import kipoderax.virtuallotto.auth.service.LostAccount;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@PropertySource(value={"classpath:messages.properties"})
public class EmailController {

    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserService userService;

    private static int userId = 0;
    private static String linkPassword = "";

    @Value("${mail.body}")
    private String body;

    @Value("${mail.subject}")
    private String subject;

    public EmailController(EmailSender emailSender,
                           UserRepository userRepository,
                           UserTokenRepository userTokenRepository,
                           UserService userService,
                           StatisticsService statisticsService,
                           HistoryGameDtoService historyGameDtoService) {

        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.userService = userService;
        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
    }

    @GetMapping({"/send-mail"})
    public String sendEmail(Model model) {

        model.addAttribute("mail", new Email());

        return "auth/lost-account";
    }



    @PostMapping("/send-mail")
    public String send(Model model, @ModelAttribute Email email) {

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());

        Optional<User> existsMail = userRepository.findByEmail(email.getAddress());

        linkPassword = LostAccount.randomStringGenerator();

        email.setBody(body + linkPassword + " Link aktywny przez 24 godziny.");
        email.setSubject(subject);

        if (existsMail.isPresent() && userTokenRepository.amountToken(existsMail.get().getId()) != 1) {

            emailSender.sendEmail(email);
            userTokenRepository.saveToken(existsMail.get().getId(), linkPassword, new Date(), 1);
            return "redirect:/";
        }

        emailSender.sendEmail(email);
        userTokenRepository.updateToken(linkPassword, existsMail.get().getId(), new Date(), 1);

        return "redirect:/send-mail";
    }

    @GetMapping("/linkPassword={linkPassword}")
    public String sendd(Model model, @PathVariable("linkPassword") String linkPasswords) {

        userId = userTokenRepository.findUserMailByToken(linkPasswords);

        model.addAttribute("passwordForm", new RegisterForm());

        System.out.println("Czas: " + emailSender.tokenRemaining(new Date(), userTokenRepository.getTime(userId)));

        if (emailSender.tokenRemaining(new Date(), userTokenRepository.getTime(userId)) > 1800000) { //30 min

            userTokenRepository.updateActiveLinkByUserId(userId, 0);
        }


        linkPassword = userTokenRepository.findTokenByUserId(userId);

        if (userTokenRepository.findActiveLinkByEmail(userId) == 0) {

            System.out.println("active: " + userTokenRepository.findActiveLinkByEmail(userId));
            return "redirect:/send-mail";
        }

        return "auth/change-password-by-link";
    }

    @PostMapping("/linkPassword={linkPassword}")
    public String sendEmail(RegisterForm registerForm, @PathVariable("linkPassword") String linkPasswords) {

        String linkPassword = userTokenRepository.findTokenByUserId(userId);

        if (userService.changePasswordViaLink(registerForm, userId)) {

            userTokenRepository.updateActiveLinkByUserId(userId, 0);
            return "redirect:/login";
        }

        return "redirect:/linkPassword=" + linkPassword;
    }
}
