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

    @Query("select g.numberGame from Game g join g.user u on g.user = u.id where u.login=:login")
    Integer findNumberGameByLogin(@Param("login") String login);
    @Query("select g.countOfThree from Game g join g.user u on g.user = u.id where u.login=:login")
    Integer findCountOfThreeByLogin(@Param("login") String login);
    @Query("select g.countOfFour from Game g join g.user u on g.user = u.id where u.login=:login")
    Integer findCountOfFourByLogin(@Param("login") String login);
    @Query("select g.countOfFive from Game g join g.user u on g.user = u.id where u.login=:login")
    Integer findCountOfFiveByLogin(@Param("login") String login);
    @Query("select g.countOfSix from Game g join g.user u on g.user = u.id where u.login=:login")
    Integer findCountOfSixByLogin(@Param("login") String login);

    @Transactional
    @Modifying
    @Query("update Game g set g.numberGame=:numberGame where g.user in " +
            "(select u.id from User u where g.user = u.id and u.id=:id)")
    void updateNumberGame(@Param("numberGame") int numberGame,
                          @Param("id") int id);
    @Transactional
    @Modifying
    @Query("update Game g set g.countOfThree=:countOfThree where g.user in " +
            "(select u.id from User u where g.user = u.id and u.id=:id)")
    void updateAmountOfThree(@Param("countOfThree") int countOfThree,
                             @Param("id") int id);
    @Transactional
    @Modifying
    @Query("update Game g set g.countOfFour=:countOfFour where g.user in " +
            "(select u.id from User u where g.user = u.id and u.id=:id)")
    void updateAmountOfFour(@Param("countOfFour") int countOfFour,
                            @Param("id") int id);
    @Transactional
    @Modifying
    @Query("update Game g set g.countOfFive=:countOfFive where g.user in " +
            "(select u.id from User u where g.user = u.id and u.id=:id)")
    void updateAmountOfFive(@Param("countOfFive") int countOfFive,
                            @Param("id") int id);
    @Transactional
    @Modifying
    @Query("update Game g set g.countOfSix=:countOfSix where g.user in " +
            "(select u.id from User u where g.user = u.id and u.id=:id)")
    void updateAmountOfSix(@Param("countOfSix") int countOfSix,
                           @Param("id") int id);

}