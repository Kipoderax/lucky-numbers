package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {

    Optional<Game> findBySaldo(int saldo);

}