package kipoderax.virtuallotto.auth.entity;

import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.entity.UserBets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data @AllArgsConstructor @NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String login;

    private String password;

    private String email;

    private int saldo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Game> game;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBets> userBets;

}