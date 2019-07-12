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

    @Query("select sum(hg.amountBets) from HistoryGame hg join hg.user u on hg.user = u.id" +
            " where hg.user.username=:username")
    Integer amountBets(@Param("username") String username);

    @Query("select sum(hg.amountGoalThrees) from HistoryGame hg join hg.user u on hg.user = u.id" +
            " where hg.user.username=:username")
    Integer amountGoalThrees(@Param("username") String username);

    @Query("select sum(hg.amountGoalFours) from HistoryGame hg join hg.user u on hg.user = u.id" +
            " where hg.user.username=:username")
    Integer amountGoalFours(@Param("username") String username);

    @Query("select sum(hg.amountGoalFives) from HistoryGame hg join hg.user u on hg.user = u.id" +
            " where hg.user.username=:username")
    Integer amountGoalFives(@Param("username") String username);

    @Query("select sum(hg.amountGoalSixes) from HistoryGame hg join hg.user u on hg.user = u.id" +
            " where hg.user.username=:username")
    Integer amountGoalSixes(@Param("username") String username);

    @Query("select sum(hg.experience) from HistoryGame hg join  hg.user u on hg.user = u.id" +
            " where hg.user.username=:username")
    Integer userExperience(@Param("username") String username);

    @Query(value = "select history_game_id from history_game order by history_game_id desc limit 1", nativeQuery = true)
    Integer findMaxId();

//    @Query(value = "SELECT hg.experience FROM history_game hg join hg.user u on hg.user where user_id := user_id order by history_game_id desc limit 1"
//            , nativeQuery = true)
//    Integer findLastGame(@Param("user_id") int userId);
}
