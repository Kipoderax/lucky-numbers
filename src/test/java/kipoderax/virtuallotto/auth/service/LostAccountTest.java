package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.tags.ServiceTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LostAccountTest implements ServiceTests {

    @Test
    void randomStringGenerator() {
        assertThat(LostAccount.randomStringGenerator().length()).isEqualTo(30);
    }
}