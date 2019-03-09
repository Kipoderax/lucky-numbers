package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.forms.LoginForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.model.GameNoEntity;
import kipoderax.virtuallotto.game.repository.GameRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
@Data
public class GameService {
    private int currentSaldo;
    private int count;
    private int myWin;

    private SecureRandom randomNumber;

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private UserSession userSession;

    private GameModel gameModel = new GameModel();
    private Game game;
    private GameNoEntity gameNoEntity;
    private GameVersionService gameVersionService;

    private LoginForm loginForm;

    public GameService(GameRepository gameRepository, UserRepository userRepository,
                       GameNoEntity gameNoEntity, GameVersionService gameVersionService,
                       UserSession userSession) {
        this.randomNumber = new SecureRandom();

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.userSession = userSession;

        this.game = new Game();
        this.gameNoEntity = gameNoEntity;
        this.gameVersionService = gameVersionService;

//        this.gameModel = new GameModel();
    }

    //SHOW TARGET
    public List<Integer> showTarget() {

        return new ArrayList<>(Arrays.asList(gameModel.getTargetEasyVersion()));
    }

    //GENERATE NUMBER
    public Set<Integer> generateNumber(GameModel gameModel) {

        while (gameModel.getNumberSet().size() != 6) {

            gameModel.setNumber(randomNumber.nextInt(25) + 1);
            gameModel.getNumberSet().add(gameModel.getNumber());
        }

        return gameModel.getNumberSet();
    }

    //GOAL NUMBER
    public List<Integer> addGoalNumber(GameModel gameModel, GameNoEntity gameNoEntity) {
//        gameModel.getAddGoalNumbers().clear();
//        Game game = new Game();

        currentSaldo = gameNoEntity.getSaldo();
        count = 0;
        for (int value : gameModel.getNumberSet()) {

            for (int i = 0; i < gameModel.getNumberSet().size(); i++) {

                if (value == gameModel.getTargetEasyVersion()[i]) {

                    gameModel.getAddGoalNumbers().add(gameModel.getTargetEasyVersion()[i]);
                    count++;
                }
            }
        }

        upgradeCurrentSaldo(gameNoEntity);

        return gameModel.getAddGoalNumbers();
    }

    //UPGRADE SALDO
    public void upgradeCurrentSaldo(GameNoEntity gameNoEntity) {
        myWin = 0;

        for (int i = 3; i <= gameNoEntity.getRewards().length; i++) {

            if (count == i) {

                currentSaldo += gameNoEntity.getRewards()[i - 2] + gameNoEntity.getRewards()[0];
                gameNoEntity.setSaldo(currentSaldo);
                myWin = gameNoEntity.getRewards()[i-2];
            }
        }

        if (count < 3) {

            currentSaldo += gameNoEntity.getRewards()[0];
            gameNoEntity.setSaldo(currentSaldo);
        }
    }

    //GET SALDO
    public int getSaldo(GameNoEntity gameNoEntity) {

        return gameNoEntity.getSaldo();
    }

    //GET ACTUAL WIN
    public int getMyWin() {

        return myWin;
    }

    //CHARGE SALDO
    public void chargeSaldo(int saldo) {

       userRepository.updateUserSaldoByLogin(
               saldo + userSession.getUser().getSaldo(), userSession.getUser().getLogin());

//       return userSession.getUser().getSaldo();
    }

    //INPUT NUMBERS
    public void inputNumbers(int number) {

        Set<Integer> numbersSet = new TreeSet<>();
        gameModel.setNumber(number);
        numbersSet.add(gameModel.getNumber());

    }
}