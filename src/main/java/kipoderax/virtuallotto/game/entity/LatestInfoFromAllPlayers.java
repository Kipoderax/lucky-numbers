package kipoderax.virtuallotto.game.entity;

import javax.persistence.*;

@Entity
@Table(name = "latest_info_from_all_players")
public class LatestInfoFromAllPlayers {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "total_gain_id") int totalGainId;

    private String date;
    private @Column(name = "total_send_bets") int totalSendBets;
    private @Column(name = "total_score_of_threes") int totalScoreOfThrees;
    private @Column(name = "total_score_of_fours") int totalScoreOfFours;
    private @Column(name = "total_score_of_fives") int totalScoreOfFives;
    private @Column(name = "total_score_of_sixes") int totalScoreOfSixes;

}
