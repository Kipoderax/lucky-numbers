package kipoderax.virtuallotto.auth.entity;

import kipoderax.virtuallotto.game.entity.ApiNumber;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.entity.UserBets;
import kipoderax.virtuallotto.game.entity.UserExperience;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    private String email;

    private int saldo;

    private int active;

    @Column(name = "date_of_created_account")
    private Date dateOfCreatedAccount;

    @Column(name = "last_login")
    private Date lastLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Game game;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBets> userBets;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_exp_id")
    private UserExperience userExperience;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "id_api_numbers")
    private ApiNumber apiNumber;

    @OneToMany(mappedBy = "user", cascade = {
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<HistoryGame> historyGames;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_token")
    private UserToken userToken;

}