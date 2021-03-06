package kipoderax.virtuallotto.game.entity;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "idgame") Integer idGame;

    private @Column(name = "max_bets_to_send") int maxBetsToSend;
    private @Column(name = "count_of_three") int countOfThree;
    private @Column(name = "count_of_four") int countOfFour;
    private @Column(name = "count_of_five") int countOfFive;
    private @Column(name = "count_of_six") int countOfSix;
    private @Column(name = "number_game") int numberGame;
    private int profit;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Game{" +
                "idGame=" + idGame +
                ", maxBetsToSend=" + maxBetsToSend +
                ", countOfThree=" + countOfThree +
                ", countOfFour=" + countOfFour +
                ", countOfFive=" + countOfFive +
                ", countOfSix=" + countOfSix +
                ", numberGame=" + numberGame +
                ", profit=" + profit +
                '}';
    }
}