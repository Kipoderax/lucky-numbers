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

    private @Column(name = "type_game") String typeGame;
    private @Column(name = "count_of_three") int countOfThree;
    private @Column(name = "count_of_four") int countOfFour;
    private @Column(name = "count_of_five") int countOfFive;
    private @Column(name = "count_of_six") int countOfSix;
    private @Column(name = "number_game") int numberGame;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id")
    private User user;

}