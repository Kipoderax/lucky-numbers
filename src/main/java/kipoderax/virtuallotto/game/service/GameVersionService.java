package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.game.model.GameModel;
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

    //REAL VERSION
    //todo może by jaki polimorfizm dla metod której zmienną będzie losowania z ilości liczb 25-49

}