package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.commons.forms.LoginForm;
import kipoderax.virtuallotto.commons.forms.RegisterForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.validation.RegexValidation;
import kipoderax.virtuallotto.game.entity.ApiNumber;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.entity.UserExperience;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class UserService {

    public enum Response {
        SUCCESS, FAILED
    }

    @Value("${error.usernameExist}")
    private String usernameExist;
    @Value(("${error.emailExist}"))
    private String emailExist;
    @Value("${error.passwordNotMatch}")
    private String shortPassword;
    @Value("${error.usernameIsTooShort}")
    private String shortUsername;

    private final UserSession userSession;

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserExperienceRepository userExperienceRepository;
    private final ApiNumberRepository apiNumberRepository;

    public UserService(UserRepository userRepository,
                       UserSession userSession,
                       GameRepository gameRepository,
                       UserExperienceRepository userExperienceRepository,
                       ApiNumberRepository apiNumberRepository) {

        this.userSession = userSession;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userExperienceRepository = userExperienceRepository;
        this.apiNumberRepository = apiNumberRepository;
    }

    public boolean register(RegisterForm registerForm, Model model) {
        GameModel gameModel = new GameModel();

        User user = new User();
        Game game = new Game();
        UserExperience userExperience = new UserExperience();
        ApiNumber apiNumber = new ApiNumber();

        if (isUsernameFree(registerForm.getUsername().toLowerCase())){

            model.addAttribute("username", usernameExist);
            return false;
        }

        if (!RegexValidation.isCorrectPattern(RegexValidation.USERNAME_PATTERN, registerForm.getUsername())) {
            model.addAttribute("usernameIsTooShort", shortUsername);
            return false;
        }

        if (isEmailFree(registerForm.getEmail())) {

            model.addAttribute("email", emailExist);
            return false;
        }

        user.setUsername(registerForm.getUsername());

        if (registerForm.getPassword().equals(registerForm.getConfirmPassword())) {

            if (registerForm.getPassword().length() > 7) {
                user.setPassword(bCryptPasswordEncoder().encode(registerForm.getPassword()));
            } else {

                model.addAttribute("short", shortPassword);
                return false;
            }
        } else { return false; }

        user.setEmail(registerForm.getEmail());
        user.setSaldo(30);
        user.setDateOfCreatedAccount(new Date());

        game.setMaxBetsToSend(10);
        game.setNumberGame(0);
        game.setCountOfThree(0);
        game.setCountOfFour(0);
        game.setCountOfFive(0);
        game.setCountOfSix(0);
        game.setProfit(0);

        userExperience.setLevel(1);
        userExperience.setExperience(0);

        apiNumber.setNumber1(gameModel.getConvertToJson().getLastLottoNumbers().get(0));
        apiNumber.setNumber2(gameModel.getConvertToJson().getLastLottoNumbers().get(1));
        apiNumber.setNumber3(gameModel.getConvertToJson().getLastLottoNumbers().get(2));
        apiNumber.setNumber4(gameModel.getConvertToJson().getLastLottoNumbers().get(3));
        apiNumber.setNumber5(gameModel.getConvertToJson().getLastLottoNumbers().get(4));
        apiNumber.setNumber6(gameModel.getConvertToJson().getLastLottoNumbers().get(5));

        game.setUser(user);
        user.setGame(game);
        userExperience.setUser(user);
        apiNumber.setUser(user);

        gameRepository.save(game);
        userRepository.save(user);
        userExperienceRepository.save(userExperience);
        apiNumberRepository.save(apiNumber);

        return true;
    }

    private boolean isUsernameFree(String username) {

        return userRepository.existsByUsername(username.toLowerCase());
    }


    private boolean isEmailFree(String email) {

        return userRepository.existsByEmail(email);
    }

    private boolean isCorrectCurrentPassword(String password, Optional<User> userOptional) {

        return bCryptPasswordEncoder().matches(
                password, userOptional.get().getPassword()
        );
    }

    public Response login(LoginForm loginForm) {
        Optional<User> userOptional =
                userRepository.findByUsername(loginForm.getUsername());

        if (!userOptional.isPresent()) {

            return Response.FAILED;
        }
        if (!isCorrectCurrentPassword(loginForm.getPassword(), userOptional)) {

            return Response.FAILED;
        }

        userSession.setUsername(loginForm.getUsername());
        userSession.setUserLogin(true);
        userSession.setUser(userOptional.get());

        return Response.SUCCESS;
    }

    public void logout() {

        userSession.setUserLogin(false);
        userSession.setUser(null);
    }

    public void deleteAccount(int userId) {

        userRepository.deleteAccountByUserId(userId);
    }

    public boolean changePassword(RegisterForm registerForm) {

        if (!isCorrectCurrentPassword(registerForm)) {

            return false;
        }
        if (!registerForm.getNewPassword().equals(registerForm.getConfirmNewPassword())) {

            return false;
        }
        if (registerForm.getNewPassword().length() < 8) {

            return false;
        }

        registerForm.setNewPassword(bCryptPasswordEncoder().encode(registerForm.getNewPassword()));

        userRepository.updateUserPassword(registerForm.getNewPassword(),
                userSession.getUser().getId());

        return true;
    }

    public boolean isCorrectCurrentPassword(RegisterForm registerForm) {

        return bCryptPasswordEncoder().matches(
                registerForm.getPassword(), userSession.getUser().getPassword());
    }

    public boolean changePasswordViaLink(RegisterForm registerForm, int id) {
        if (!registerForm.getNewPassword().equals(registerForm.getConfirmNewPassword())) {

            return false;
        }

        if (registerForm.getNewPassword().length() < 8){

            return false;
        }

        registerForm.setNewPassword(bCryptPasswordEncoder().encode(registerForm.getNewPassword()));

        userRepository.updateUserPassword(registerForm.getNewPassword(),
                id);

        return true;
    }

    private BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}