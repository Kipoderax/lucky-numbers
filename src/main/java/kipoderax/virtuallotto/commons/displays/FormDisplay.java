package kipoderax.virtuallotto.commons.displays;

import kipoderax.virtuallotto.commons.forms.LoginForm;
import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.commons.mail.Email;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class FormDisplay {

    public void loginForm(Model model) {

        model.addAttribute("loginForm", new LoginForm());
    }

    public void registerForm(Model model) {

        model.addAttribute("registerForm", new RegisterForm());
    }

    public void mailForm(Model model) {

        model.addAttribute("mail", new Email());
    }

    public void numbersForm(Model model) {

        model.addAttribute("numbersForm", new NumbersForm());
    }
}
