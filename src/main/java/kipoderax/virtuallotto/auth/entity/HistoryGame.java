package kipoderax.virtuallotto.auth.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "history_game")
public class HistoryGame {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "history_game_id") Integer historyGameId;

    private @Column(name = "date_game") String dateGame;
    private @Column(name = "amount_bets") int amountBets;
    private @Column(name = "amount_goal_threes") int amountGoalThrees;
    private @Column(name = "amount_goal_fours") int amountGoalFours;
    private @Column(name = "amount_goal_fives") int amountGoalFives;
    private @Column(name = "amount_goal_sixes") int amountGoalSixes;
    private int experience;
    private int result;

    @ManyToOne(cascade = {
            CascadeType.REFRESH,
            CascadeType.MERGE,
    })
    @JoinColumn(name = "user_id")
    private User user;

}
