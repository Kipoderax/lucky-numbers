package kipoderax.virtuallotto.auth.repositories;

import kipoderax.virtuallotto.auth.entity.HistoryGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryGameRepository extends JpaRepository<HistoryGame, Integer> {

    @Query("select hg from HistoryGame hg join hg.user u on hg.user = u.id" +
            " where hg.user.id=:user_id")
    List<HistoryGame> findAllById(@Param("user_id") int userId);
}
