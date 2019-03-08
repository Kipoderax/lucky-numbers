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

    private int saldo;

//    @OneToOne(mappedBy = "game", cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE,
//            CascadeType.DETACH,
//            CascadeType.REFRESH
//    })
//    private User user;

}