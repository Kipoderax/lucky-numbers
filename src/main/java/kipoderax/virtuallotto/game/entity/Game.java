package kipoderax.virtuallotto.game.entity;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "idgame") Integer idGame;

    private @Column(name = "count_of_three") int countOfThree;
    private @Column(name = "count_of_four") int countOfFour;
    private @Column(name = "count_of_five") int countOfFive;
    private @Column(name = "count_of_six") int countOfSix;
    private @Column(name = "number_game") int numberGame;

    //todo utworzyć relacje z tabelą user
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id")
    private User user;

}