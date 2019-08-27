package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.tags.ServiceTests;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceTest implements ServiceTests {

    private Experience exp;

    @BeforeEach
    void setUp() {
        exp = new Experience();
    }

    @Test
    void currentLevel() {

        int experience = 89;
        assertThat(exp.currentLevel(experience)).isEqualTo(12);

        experience = 100;
        assertThat(exp.currentLevel(experience)).isEqualTo(12);

        experience = 107;
        assertThat(exp.currentLevel(experience)).isEqualTo(12);

        experience = 0;
        Assert.assertEquals(1, exp.currentLevel(experience));

        experience = 1;
        Assert.assertEquals(2, exp.currentLevel(experience));
    }

    @Test
    void needExpToNextLevel() {
        int experience = 89;
        assertThat(exp.needExpToNextLevel(experience)).isEqualTo(19);

        experience = 100;
        assertThat(exp.needExpToNextLevel(experience)).isEqualTo(8);

        experience = 108;
        assertThat(exp.needExpToNextLevel(experience)).isEqualTo(22);

        experience = 0;
        assertThat(exp.needExpToNextLevel(experience)).isEqualTo(1);
    }

    @Test
    void needExpForAllLevel() {

        int needExperience = 89;
        assertThat(exp.needExpForAllLevel(needExperience)).isEqualTo(19);

        needExperience = 100;
        assertThat(exp.needExpForAllLevel(needExperience)).isEqualTo(19);

        needExperience = 108;
        assertThat(exp.needExpForAllLevel(needExperience)).isEqualTo(21);

    }
}