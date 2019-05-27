package kipoderax.virtuallotto.game.entity;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Entity
@Table(name = "bets")
@Data
public class UserBets {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "idbets") int idBets;

    private @Range(min = 1, max = 49) int number1;
    private @Range(min = 1, max = 49) int number2;
    private @Range(min = 1, max = 49) int number3;
    private @Range(min = 1, max = 49) int number4;
    private @Range(min = 1, max = 49) int number5;
    private @Range(min = 1, max = 49) int number6;

    @ManyToOne(cascade = {
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST
            })
    @JoinColumn(name = "user_id")
    private User user;

}
