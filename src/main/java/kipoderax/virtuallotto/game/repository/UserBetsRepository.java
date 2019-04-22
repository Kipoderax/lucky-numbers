package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.UserBets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserBetsRepository extends JpaRepository<UserBets, Integer> {

    @Transactional
    @Modifying
    @Query(value = "insert into bets(user_id, number1, number2, number3, number4, number5, number6) "+
            "values(:id, :number1, :number2, :number3, :number4, :number5, :number6) ", nativeQuery = true)
    void saveInputNumbersByIdUser(@Param("id") int id,
                                  @Param("number1") int number1,
                                  @Param("number2") int number2,
                                  @Param("number3") int number3,
                                  @Param("number4") int number4,
                                  @Param("number5") int number5,
                                  @Param("number6") int number6);

    @Query("select ub.number1 from UserBets ub join ub.user u on ub.user = u.id" +
            " where u.login=:login and id_bets=:id")
    Integer findUserNumber1ByLogin(@Param("login") String login, int id);

    @Query("select ub.number2 from UserBets ub join ub.user u on ub.user = u.id" +
            " where u.login=:login and id_bets=:id")
    Integer findUserNumber2ByLogin(@Param("login") String login, int id);

    @Query("select ub.number3 from UserBets ub join ub.user u on ub.user = u.id" +
            " where u.login=:login and id_bets=:id")
    Integer findUserNumber3ByLogin(@Param("login") String login, int id);

    @Query("select ub.number4 from UserBets ub join ub.user u on ub.user = u.id" +
            " where u.login=:login and id_bets=:id")
    Integer findUserNumber4ByLogin(@Param("login") String login, int id);

    @Query("select ub.number5 from UserBets ub join ub.user u on ub.user = u.id" +
            " where u.login=:login and id_bets=:id")
    Integer findUserNumber5ByLogin(@Param("login") String login, int id);

    @Query("select ub.number6 from UserBets ub join ub.user u on ub.user = u.id" +
            " where u.login=:login and id_bets=:id")
    Integer findUserNumber6ByLogin(@Param("login") String login, int id);

}
