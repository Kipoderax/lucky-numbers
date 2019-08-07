package kipoderax.virtuallotto.commons.mail;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.repositories.UserTokenRepository;
import kipoderax.virtuallotto.auth.service.LostAccount;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.commons.displays.MainPageDisplay;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
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

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserService userService;
    private MainPageDisplay mainPageDisplay;

    private int userId = 0;
    private String linkPassword = "";

    @Value("${mail.body}")
    private String body;

    @Value("${mail.subject}")
    private String subject;

    @Value("${error.emailNotExist}")
    private String emailNotExist;

    @Value("${mail.sended}")
    private String mailSended;

    public EmailController(EmailSender emailSender,
                           UserRepository userRepository,
                           UserTokenRepository userTokenRepository,
                           UserService userService,
                           MainPageDisplay mainPageDisplay) {

        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.userService = userService;
        this.mainPageDisplay = mainPageDisplay;
    }

    @GetMapping({"/send-mail"})
    public String sendEmail(Model model) {

        model.addAttribute("mail", new Email());

        mainPageDisplay.displayGameStatus(model);

        return "auth/lost-account";
    }



    @PostMapping("/send-mail")
    public String send(Model model, @ModelAttribute Email email) {

        Optional<User> existsMail = userRepository.findByEmail(email.getAddress());

        linkPassword = LostAccount.randomStringGenerator();

        email.setBody(body + linkPassword + " Link aktywny przez 24 godziny.");
        email.setSubject(subject);

        if (existsMail.isPresent() && userTokenRepository.amountToken(existsMail.get().getId()) != 1) {

            emailSender.sendEmail(email);
            userTokenRepository.saveToken(existsMail.get().getId(), linkPassword, new Date(), 1);
            model.addAttribute("mailSended", mailSended);
            return "auth/lost-account";
        }
        if (!existsMail.isPresent()){

            mainPageDisplay.displayGameStatus(model);
            model.addAttribute("mailNotExist", emailNotExist);
            return "auth/lost-account";
        }

        model.addAttribute("mailSended", mailSended);
        mainPageDisplay.displayGameStatus(model);

        emailSender.sendEmail(email);
        userTokenRepository.updateToken(linkPassword, existsMail.get().getId(), new Date(), 1);

        return "auth/lost-account";
    }

    @GetMapping("/linkPassword={linkPassword}")
    public String sendd(Model model, @PathVariable("linkPassword") String linkPasswords) {

        userId = userTokenRepository.findUserMailByToken(linkPasswords);

        model.addAttribute("passwordForm", new RegisterForm());

        if (emailSender.tokenRemaining(new Date(), userTokenRepository.getTime(userId)) > 86400000) { //24h

            userTokenRepository.updateActiveLinkByUserId(userId, 0);
        }


        linkPassword = userTokenRepository.findTokenByUserId(userId);

        if (userTokenRepository.findActiveLinkByEmail(userId) == 0) {

            return "redirect:/send-mail";
        }

        mainPageDisplay.displayGameStatus(model);

        return "auth/change-password-by-link";
    }

    @PostMapping("/linkPassword={linkPassword}")
    public String sendEmail(Model model, RegisterForm registerForm, @PathVariable("linkPassword") String linkPasswords) {

        String linkPassword = userTokenRepository.findTokenByUserId(userId);

        if (userService.changePasswordViaLink(registerForm, userId)) {

            userTokenRepository.updateActiveLinkByUserId(userId, 0);
            return "redirect:/login";
        }

        mainPageDisplay.displayGameStatus(model);

        return "redirect:/linkPassword=" + linkPassword;
    }
}
