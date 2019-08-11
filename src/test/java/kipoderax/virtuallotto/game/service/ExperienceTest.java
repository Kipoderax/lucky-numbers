package kipoderax.virtuallotto.game.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExperienceTest {

    private Experience exp;

    @BeforeEach
    void setUp() {
        exp = new Experience();
    }

    @Test
    void currentLevel() {

        int experience = 100;
        Assert.assertEquals(12, exp.currentLevel(experience));

        experience = 50;
        Assert.assertEquals(9, exp.currentLevel(experience));

        experience = 9;
        Assert.assertEquals(4, exp.currentLevel(experience));

        experience = 10;
        Assert.assertEquals(5, exp.currentLevel(experience));

        experience = 0;
        Assert.assertEquals(1, exp.currentLevel(experience));

        experience = 1;
        Assert.assertEquals(2, exp.currentLevel(experience));
    }

    @Test
    void needExpToNextLevel() {
        int experience = 65;
        Assert.assertEquals(6, exp.needExpToNextLevel(experience));

        experience = 2395;
        Assert.assertEquals(7, exp.needExpToNextLevel(experience));
    }

    @Test
    void needExpForAllLevel() {
        int experience = 65;
        Assert.assertEquals(15, exp.needExpForAllLevel(experience));

        experience = 2395;
        Assert.assertEquals(131, exp.needExpForAllLevel(experience));
    }
}