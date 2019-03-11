package kipoderax.virtuallotto.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data @AllArgsConstructor @NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String login;

    private String password;

    private String email;

    private int saldo;

    //todo powiązać z tabelą user, do przechowywania i strzałów (i = 3, ..., 6) z n gier
//    @OneToOne(cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE,
//            CascadeType.DETACH,
//            CascadeType.REFRESH
//    })
//    @JoinColumn(name = "")
//    private Game game;

}