package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class GameService {
    private SecureRandom randomNumber;

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private GameModel gameModel = new GameModel();
    private Game game;

    private LoginForm loginForm;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.randomNumber = new SecureRandom();

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;

        this.game = new Game();

//        this.gameModel = new GameModel();
    }

    //GENERATE NUMBER
    public Set<Integer> generateNumber(GameModel gameModel) {
        while (gameModel.getNumberSet().size() != 6) {
            gameModel.setNumber(randomNumber.nextInt(19) + 1);
            gameModel.getNumberSet().add(gameModel.getNumber());
        }

        return gameModel.getNumberSet();
    }

    //GOAL NUMBER
    public List<Integer> addGoalNumber(GameModel gameModel, String login) {
//        gameModel.getAddGoalNumbers().clear();
//        Game game = new Game();

        int count = 0;
        for (int value : gameModel.getNumberSet()) {

            for (int i = 0; i < gameModel.getNumberSet().size(); i++) {

                if (value == gameModel.getTarget()[i]) {
                    gameModel.getAddGoalNumbers().add(gameModel.getTarget()[i]);
                    count++;
                }
            }
        }
        gameModel.setCount(count);

        if (gameModel.getCount() > 0) {
            game.setSaldo(100);
        }

        return gameModel.getAddGoalNumbers();
    }

    //SHOW TARGET
    public List<Integer> showTarget() {

        return new ArrayList<>(Arrays.asList(gameModel.getTarget()));
    }

    //UPDATE SALDO

}