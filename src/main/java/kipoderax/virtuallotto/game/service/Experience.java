package kipoderax.virtuallotto.game.service;

import org.springframework.stereotype.Service;

/**
 *  experience formula (describe later)
 */

@Service
public class Experience {

    public int reachNextLevel(int experience) {

        return experience == 0 ? 1 : (int) (2 * Math.pow(experience, 0.4));
    }

    public int needExpToNextLevel(int level, int exp) {

        return (int) ((0.176777 * Math.pow(level + 1, 2.5) + 1) - (exp));
    }

}
