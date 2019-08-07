package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.LatestInfoFromAllPlayers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LatestInfoFromAllPlayersRepository extends JpaRepository<LatestInfoFromAllPlayers, Integer> {

    @Query(value = "select date from latest_info_from_all_players order by total_gain_id desc limit 1", nativeQuery = true)
    String findDate();

    @Query(value = "select date from latest_info_from_all_players where total_gain_id = " +
            "(select max(total_gain_id) - 1 from latest_info_from_all_players)", nativeQuery = true)
    String findPreviousDate();

    @Query(value = "select total_send_bets from latest_info_from_all_players order by total_gain_id desc limit 1", nativeQuery = true)
    Integer findTotalSendBets();

    @Query(value = "select total_score_of_threes from latest_info_from_all_players order by total_gain_id desc limit 1", nativeQuery = true)
    Integer findTotalScoreOfThrees();

    @Query(value = "select total_score_of_fours from latest_info_from_all_players order by total_gain_id desc limit 1", nativeQuery = true)
    Integer findTotalScoreOfFours();

    @Query(value = "select total_score_of_fives from latest_info_from_all_players order by total_gain_id desc limit 1", nativeQuery = true)
    Integer findTotalScoreOfFives();

    @Query(value = "select total_score_of_sixes from latest_info_from_all_players order by total_gain_id desc limit 1", nativeQuery = true)
    Integer findTotalScoreOfSixes();
}
