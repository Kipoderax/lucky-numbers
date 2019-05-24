package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.UserBets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserBetsRepository extends JpaRepository<UserBets, Integer> {

    @Transactional
    @Modifying
    @Query(value = "insert into bets(user_id, number1, number2, number3, number4, number5, number6) "+
            "values(:id, :number1, :number2, :number3, :number4, :number5, :number6)", nativeQuery = true)
    void saveInputNumbersByIdUser(@Param("id") int id,
                                  @Param("number1") int number1,
                                  @Param("number2") int number2,
                                  @Param("number3") int number3,
                                  @Param("number4") int number4,
                                  @Param("number5") int number5,
                                  @Param("number6") int number6);

    @Query("select ub from UserBets ub join ub.user u on ub.user = u.id" +
            " where ub.user.id=:user_id")
    List<UserBets> findAllById(@Param("user_id") int userId);

    @Query("select count(ub) from UserBets ub join ub.user u on ub.user = u.id" +
            " where ub.user.id=:user_id")
    Integer AmountBetsByUserId(@Param("user_id") int userId);

    @Query("delete UserBets where id=:user_id")
    void deleteUserBetsAfterShowResult(@Param("user_id") int userId);

}
