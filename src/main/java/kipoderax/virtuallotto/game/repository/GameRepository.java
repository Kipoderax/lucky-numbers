package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

//    Optional<Game> getBySaldo();

//    @Query(value = "select saldo from game")
    Game getGameBySaldo(String login);

}