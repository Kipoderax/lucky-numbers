package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.forms.RegisterForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.dtos.mapper.UserMapper;
import kipoderax.virtuallotto.dtos.models.UserDto;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.service.GameService;
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
    private final UserMapper mapper;

    private final GameRepository gameRepository;
    private final GameService gameService;

    @Autowired
    public UserService(UserRepository userRepository, UserSession userSession,
                       UserMapper mapper,
                       GameRepository gameRepository,
                       GameService gameService) {

        this.userRepository = userRepository;
        this.userSession = userSession;
        this.mapper = mapper;

        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

    public boolean register(RegisterForm registerForm) {
        User user = new User();
        Game game = new Game();

        if (isLoginFree(registerForm.getLogin())){

            return false;
        }

        user.setLogin(registerForm.getLogin());
        user.setPassword(bCryptPasswordEncoder().encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setSaldo(5000);

//        game.setSaldo(10000);
//        user.setGame(game);

//        gameRepository.save(game);
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
//        userSession.setGame(gameRepository.getGameBySaldo(loginForm.getLogin()));

        return LoginResponse.SUCCESS;
    }

    public void logout() {

        userSession.setUserLogin(false);
        userSession.setUser(null);

    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    public List<UserDto> getuserDto(String login) {
        List<UserDto> userDto = new ArrayList<>();


        for (User u : userRepository.findAllByLogin(login)) {

            userDto.add(mapper.map(u));
        }

        return userDto;
    }

}