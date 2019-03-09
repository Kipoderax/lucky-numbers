package kipoderax.virtuallotto.game.entity;

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
    private Integer id;

    private @Column(name = "count_of_three") int countOfThree;
    private @Column(name = "count_of_four") int countOfFour;
    private @Column(name = "count_of_five") int countOfFive;
    private @Column(name = "count_of_six") int countOfSix;

    //todo utworzyć relacje z tabelą user
//    @OneToOne(mappedBy = "game", cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE,
//            CascadeType.DETACH,
//            CascadeType.REFRESH
//    })
//    private User user;

}