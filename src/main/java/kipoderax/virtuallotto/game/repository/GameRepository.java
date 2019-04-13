package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query("select numberGame from Game")
    Integer findNumberGame();
    @Query("select countOfThree from Game")
    Integer findCountOfThree();
    @Query("select countOfFour from Game")
    Integer findCountOfFour();
    @Query("select countOfFive from Game")
    Integer findCountOfFive();
    @Query("select countOfSix from Game")
    Integer findCountOfSix();

    @Transactional
    @Modifying
    @Query("update Game set numberGame=:numberGame")
    void updateNumberGame(@Param("numberGame") int numberGame);

    @Transactional
    @Modifying
    @Query("update Game set countOfThree=:countOfThree")
    void updateAmountOfThree(@Param("countOfThree") int countOfThree);
    @Transactional
    @Modifying
    @Query("update Game set countOfFour=:count")
    void updateAmountOfFour(@Param("count") int count);
    @Transactional
    @Modifying
    @Query("update Game set countOfFive=:count")
    void updateAmountOfFive(@Param("count") int count);
    @Transactional
    @Modifying
    @Query("update Game set countOfSix=:count")
    void updateAmountOfSix(@Param("count") int count);

}