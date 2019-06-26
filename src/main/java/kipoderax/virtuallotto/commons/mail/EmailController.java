package kipoderax.virtuallotto.commons.mail;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.LostAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Optional;

@Controller
@PropertySource(value={"classpath:messages.properties"})
public class EmailController {

    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    private String slinkPassword = LostAccount.randomStringGenerator();

    @Value("${mail.body}")
    private String body;

    @Value("${mail.subject}")
    private String subject;

    public EmailController(EmailSender emailSender, TemplateEngine templateEngine,
                           UserRepository userRepository) {

        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @GetMapping({"/send-mail"})
    public String sendEmail(Model model) {

        model.addAttribute("mail", new Email());

        return "auth/lost-account";
    }



    @PostMapping("/send-mail")
    public String send(Model model, @ModelAttribute Email email) {

        Optional<User> existsMail = userRepository.findByEmail(email.getAddress());

        email.setBody(body + slinkPassword);
        email.setSubject(subject);

        if (existsMail.isPresent()) {

            emailSender.sendEmail(email);
            return "redirect:/";
        }

        return "redirect:/send-mail";
    }
}
