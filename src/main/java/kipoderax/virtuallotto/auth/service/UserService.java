package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.forms.RegisterForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.dtos.mapper.UserMapper;
import kipoderax.virtuallotto.dtos.models.UserDto;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.entity.UserExperience;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    public enum LoginResponse {
        SUCCESS, FAILED
    }

    private final UserSession userSession;
    private UserMapper userMapper;

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserExperienceRepository userExperienceRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserSession userSession,
                       GameRepository gameRepository,
                       UserExperienceRepository userExperienceRepository,
                       UserMapper userMapper) {

        this.userSession = userSession;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userExperienceRepository = userExperienceRepository;
        this.userMapper = userMapper;

    }

    public boolean register(RegisterForm registerForm) {
        User user = new User();
        Game game = new Game();
        UserExperience userExperience = new UserExperience();

        if (isLoginFree(registerForm.getLogin())){

            return false;
        }

        user.setUsername(registerForm.getUsername());
        user.setLogin(registerForm.getLogin());
        user.setPassword(bCryptPasswordEncoder().encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setSaldo(100);
        user.setDateOfCreatedAccount(new Date());

        game.setNumberGame(0);
        game.setCountOfThree(0);
        game.setCountOfFour(0);
        game.setCountOfFive(0);
        game.setCountOfSix(0);

        userExperience.setLevel(1);
        userExperience.setExperience(0);

        game.setUser(user);
        user.setGame(game);
        userExperience.setUser(user);

        gameRepository.save(game);
        userRepository.save(user);
        userExperienceRepository.save(userExperience);

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

    public int getCountPlayers() {

       return userRepository.getAllRegisterUsers();
    }

//    public String getDateOfRegisterUser(String login) {
//        userRepository.findDateOfCreateAccountByLogin(login);
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.GERMAN);
//        return dateFormat.format(userRepository.findDateOfCreateAccountByLogin(login));
//    }

}