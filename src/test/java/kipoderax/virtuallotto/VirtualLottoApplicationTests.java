package kipoderax.virtuallotto;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VirtualLottoApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreate() {

    }
}