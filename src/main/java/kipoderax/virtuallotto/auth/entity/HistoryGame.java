package kipoderax.virtuallotto.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "history_game")
@Data @NoArgsConstructor @AllArgsConstructor
public class HistoryGame {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "history_game_id") int historyGameId;
    private @Column(name = "date_game") String dateGame;
    private @Column(name = "amount_bets") int amountBets;
    private @Column(name = "amount_goal_threes") int amountGoalThrees;
    private @Column(name = "amount_goal_fours") int amountGoalFours;
    private @Column(name = "amount_goal_fives") int amountGoalFives;
    private @Column(name = "amount_goal_sixes") int amountGoalSixes;
    private int experience;
    private int result;

}
