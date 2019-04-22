package kipoderax.virtuallotto.game.entity;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "bets")
@Data
public class UserBets {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "id_bets") int idBets;

    private int number1;
    private int number2;
    private int number3;
    private int number4;
    private int number5;
    private int number6;

    @ManyToOne(cascade = {
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST
            })
    @JoinColumn(name = "user_id")
    private User user;

}
