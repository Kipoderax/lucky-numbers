package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.game.model.GameModel;
import org.springframework.stereotype.Service;

@Service
public class Experience {

    public int reachNextLevel(GameModel gameModel) {

        return (int) (2 * Math.pow(gameModel.getExperience(), 0.4));
    }

    public int needExpToNextLevel(int level, int exp) {

        return (int) ((0.176777 * Math.pow(level + 1, 2.5)) + 1) - (exp);
    }
}
