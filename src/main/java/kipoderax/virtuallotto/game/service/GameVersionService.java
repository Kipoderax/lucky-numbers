package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.model.GameNoEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GameVersionService {

    private int currentSaldo;
    private int count;
    private int myWin;

    private SecureRandom randomNumber;

    private GameModel gameModel = new GameModel();
    private Game game;
    private GameNoEntity games;

    //REAL VERSION


}