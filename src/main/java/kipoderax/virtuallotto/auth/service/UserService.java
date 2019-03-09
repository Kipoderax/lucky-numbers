package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.forms.RegisterForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public enum LoginResponse {
        SUCCESS, FAILED
    }

    private final UserRepository userRepository;
    private final UserSession userSession;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserSession userSession) {

        this.userRepository = userRepository;
        this.userSession = userSession;

    }

    public boolean register(RegisterForm registerForm) {
        User user = new User();

        if (isLoginFree(registerForm.getLogin())){

            return false;
        }

        user.setUsername(registerForm.getUsername());
        user.setLogin(registerForm.getLogin());
        user.setPassword(bCryptPasswordEncoder().encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setSaldo(5000);

        userRepository.save(user);

        return true;
    }

    public boolean isLoginFree(String login) {

        return userRepository.existsByLogin(login);
    }

    public LoginResponse login(LoginForm loginForm) {
        Optional<User> userOptional =
                userRepository.findByLogin(loginForm.getLogin());

        if (!userOptional.isPresent()) {

            return LoginResponse.FAILED;
        }
        if (!bCryptPasswordEncoder().matches(
                loginForm.getPassword(), userOptional.get().getPassword()
        )) {

            return LoginResponse.FAILED;
        }

        userSession.setUserLogin(true);
        userSession.setUser(userOptional.get());

        return LoginResponse.SUCCESS;
    }

    public void logout() {

        userSession.setUserLogin(false);
        userSession.setUser(null);

    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    //todo przygotować jakoś templatke,
    // controllera by na każdej podstronie
    // wyświetlało ilość zarejestrowanych użytkowników
    public int getCountPlayers() {

       return userRepository.getCountPlayers();
    }

}