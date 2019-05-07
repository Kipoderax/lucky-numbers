package kipoderax.virtuallotto.game.entity;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_experience")
@Data
public class UserExperience {

    @Id @Column(name = "user_exp_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userExperienceID;

    private int level;

    private int experience;

    @OneToOne(cascade =
             {CascadeType.DETACH,
              CascadeType.MERGE,
              CascadeType.PERSIST,
              CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

}
