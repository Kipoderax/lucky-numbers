package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.forms.RegisterForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public enum LoginResponse {
        SUCCESS, FAILED
    }

    private final UserRepository userRepository;
    private final UserSession userSession;

    private final GameRepository gameRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserSession userSession,
                       GameRepository gameRepository) {

        this.userRepository = userRepository;
        this.userSession = userSession;
        this.gameRepository = gameRepository;

    }

    public boolean register(RegisterForm registerForm) {
        User user = new User();
        Game game = new Game();
        List<Game> games = new ArrayList<>();

        if (isLoginFree(registerForm.getLogin())){

            return false;
        }

        user.setUsername(registerForm.getUsername());
        user.setLogin(registerForm.getLogin());
        user.setPassword(bCryptPasswordEncoder().encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setSaldo(100);

        game.setNumberGame(0);
        game.setCountOfThree(0);
        game.setCountOfFour(0);
        game.setCountOfFive(0);
        game.setCountOfSix(0);

        game.setUser(user);
        user.setGame(games);

        gameRepository.save(game);
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