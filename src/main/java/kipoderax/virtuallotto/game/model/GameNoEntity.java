package kipoderax.virtuallotto.game.model;

import kipoderax.virtuallotto.auth.service.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data @NoArgsConstructor @AllArgsConstructor
@Service
public class GameNoEntity {

    private UserSession userSession;

    public GameNoEntity(UserSession userSession) {
        this.userSession = userSession;
    }

    private int[] rewards = {-3, 24, 120, 4_800, 2_000_000};
    private int saldo;
    private int count = 0;

}