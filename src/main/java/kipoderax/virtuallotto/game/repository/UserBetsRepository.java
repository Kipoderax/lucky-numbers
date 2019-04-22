package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.UserBets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBetsRepository extends JpaRepository<UserBets, Integer> {

    //todo metody zapisujace liczby aktualnie zalogowanemu uzytkownikowi

}
