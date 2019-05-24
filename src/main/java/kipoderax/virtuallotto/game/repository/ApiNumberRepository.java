package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.ApiNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApiNumberRepository extends JpaRepository<ApiNumber, Integer> {

    @Query("select an from ApiNumber an join an.user u on an.user = u.id " +
            "where an.user.id=:user_id")
    List<ApiNumber> findAllByUserId(@Param("user_id") int id);

    @Transactional
    @Modifying
    @Query("update ApiNumber an set an.number1=:number1, an.number2=:number2, an.number3=:number3," +
            " an.number4=:number4, an.number5=:number5, an.number6=:number6 " +
            "where an.user in (select u.id from User u where an.user = u.id and u.id=:user_id)")
    void updateApiNumbers(@Param("user_id") int userId,
                          @Param("number1") int number1,
                          @Param("number2") int number2,
                          @Param("number3") int number3,
                          @Param("number4") int number4,
                          @Param("number5") int number5,
                          @Param("number6") int number6);

}
