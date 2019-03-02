package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class GameService {
    private SecureRandom randomNumber;

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private GameModel gameModel = new GameModel();
    private Game game;

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

            for(int i = 0; i < gameModel.getNumberSet().size(); i++) {

                if (value == gameModel.getTarget()[i]) {
                    gameModel.getAddGoalNumbers().add(gameModel.getTarget()[i]);
                    count++;
                }
            }
        }
        gameModel.setCount(count);

        if (gameModel.getCount() > 0) {

            int money = game.getSaldo();
            System.out.println("Przed: " + money);
            game.setSaldo(money - 25);
            int actualSaldo = game.getSaldo();
            updateSaldo(login, game.getSaldo(), actualSaldo);
            System.out.println("Po: " + money);
        }

        return gameModel.getAddGoalNumbers();
    }

    //SHOW TARGET
    public List<Integer> showTarget() {
        List<Integer> target = new ArrayList<>(Arrays.asList(gameModel.getTarget()));

        return target;
    }

    //UPDATE SALDO
    public boolean updateSaldo(String login, int saldo, int actualSaldo) {

        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isPresent()) {

            Optional<Game> gameOptional = gameRepository.findBySaldo(saldo);

            if (gameOptional.isPresent()) {
                userOptional.get().getGame().setSaldo(actualSaldo);

                userRepository.save(userOptional.get());
                gameRepository.save(game);
            }

            return true;
        }

        return false;
    }

}