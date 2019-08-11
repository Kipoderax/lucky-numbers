package kipoderax.virtuallotto.game.service;

import org.springframework.stereotype.Service;

@Service
public class Experience {

    public int currentLevel(int experience) {

        return experience == 0 ? 1 : (int) (2 * Math.pow(experience, 0.4));
    }

    public int needExpToNextLevel(int experience) {
        if (currentLevel(experience) == 1) {
            return 1;
        }
        return (int) ((0.176777 * Math.pow(currentLevel(experience) + 1, 2.5) + 1) - (experience));
    }

    public int needExpForAllLevel(int experience) {

        return (int) ((0.176777 * Math.pow(currentLevel(experience) + 1, 2.5)) -
                (0.176777 * Math.pow(currentLevel(experience), 2.5)));
    }

}
