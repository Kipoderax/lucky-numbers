package kipoderax.virtuallotto.auth.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_token")
@Data
public class UserToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column(name = "id_user_token") int id;

    private @Column(name = "token") String token;

    @Column (name = "date_creation_token")
    private Date dateCreationToken;

    private int active;

    @OneToOne(cascade = {
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST
    })
    @JoinColumn(name = "user_id")
    private User user;

}

