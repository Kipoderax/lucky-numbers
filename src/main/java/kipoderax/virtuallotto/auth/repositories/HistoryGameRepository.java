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

    @Query(value = "select history_game_id from history_game order by history_game_id desc limit 1", nativeQuery = true)
    Integer findMaxId();

//    "select g.numberGame from Game g join g.user u on g.user = u.id where u.id=:user_id"
//    @Query(value = "SELECT hg.experience FROM history_game hg join hg.user u on hg.user where user_id := user_id order by history_game_id desc limit 1"
//            , nativeQuery = true)
//    Integer findLastGame(@Param("user_id") int userId);
}
