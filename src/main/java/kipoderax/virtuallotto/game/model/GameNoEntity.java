package kipoderax.virtuallotto.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data @NoArgsConstructor @AllArgsConstructor
@Service
public class GameNoEntity {

    private int[] rewards = {-3, 24, 120, 4800, 2_000_000};
    private int saldo = 100;
    private int count = 0;

}