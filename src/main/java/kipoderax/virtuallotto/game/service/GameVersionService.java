package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.model.GameNoEntity;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Data
public class GameVersionService {

    private int currentSaldo;
    private int count;
    private int myWin;

    private SecureRandom randomNumber;

    private GameModel gameModel = new GameModel();
    private GameNoEntity games;

    //REAL VERSION


}