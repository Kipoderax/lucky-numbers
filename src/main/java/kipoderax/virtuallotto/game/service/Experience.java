package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  Exp formula: 5 * (1,03) ^ lvl (exp need to reach next lv)
 */

@Service @NoArgsConstructor
public class Experience {

    private GameService gameService;
    private UserExperienceRepository userExperienceRepository;

    public Experience(GameService gameService,
                      UserExperienceRepository userExperienceRepository) {

        this.gameService = gameService;
        this.userExperienceRepository = userExperienceRepository;
    }

    public int reachNextLevel(int experience) {
//        formulaLevel(experience, level);

        return experience / 5;
    }

    //todo cdn
    public void formulaLevel(int experience, int level) {
        level = experience / 5;
    }

}
