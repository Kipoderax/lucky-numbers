package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.auth.service.UserService;
import kipoderax.virtuallotto.dtos.mapper.UserMapper;
import kipoderax.virtuallotto.dtos.models.UserDto;
import kipoderax.virtuallotto.game.repository.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class StatisticsController {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    private UserService userService;
    private UserMapper userMapper;

    public StatisticsController(UserRepository userRepository,
                                GameRepository gameRepository,
                                UserMapper userMapper,
                                UserService userService) {

        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }
//[UserDto(username=tester, amountOfThree=1450, amountOfFour=23, amountOfFive=0, amountOfSix=0, numberGame=0, level=27, experience=713),
// UserDto(username=Kipoderax, amountOfThree=124, amountOfFour=3, amountOfFive=0, amountOfSix=0, numberGame=0, level=9, experience=166)
 @GetMapping("/statystyki")
    public String statistics(Model model) {

        model.addAttribute("userDto", userService.getAllDtoUsers());

        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());

        return "game/statistics";
    }
}
