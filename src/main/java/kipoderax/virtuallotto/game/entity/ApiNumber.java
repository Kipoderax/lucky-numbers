package kipoderax.virtuallotto.game.entity;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "api_number")
public class ApiNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "id_api_numbers") int idApiNumbers;

    private int number1;
    private int number2;
    private int number3;
    private int number4;
    private int number5;
    private int number6;

    @OneToOne(cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

}