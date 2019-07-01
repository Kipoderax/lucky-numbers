package kipoderax.virtuallotto.auth.repositories;

import kipoderax.virtuallotto.auth.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

    @Transactional
    @Modifying
    @Query(value = "insert into user_token (user_id, token, date_creation_token) values(:user_id, :token, :date_creation)",
            nativeQuery = true)
    void saveToken(@Param("user_id") int userId,
                   @Param("token") String token,
                   @Param("date_creation") Date dateCreation);

    @Transactional
    @Modifying
    @Query("update UserToken ut set ut.token=:token, ut.dateCreationToken=:date_creation where ut.user in " +
            "(select u.id from User u where ut.user = u.id and u.id=:user_id)")
    void updateToken(@Param("token") String token,
                          @Param("user_id") int userId,
                     @Param("date_creation") Date dateCreation);

    @Transactional
    @Modifying
    @Query("update UserToken ut set ut.active=:active where ut.user in " +
            "(select u.id from User u where ut.user = u.id and u.id=:user_id)")
    void updateActiveLinkByUserId(@Param("user_id") int userId,
                                  @Param("active") int active);

    @Query("select ut.active from UserToken ut join ut.user u on ut.user = u.id " +
            "where ut.user.id=:user_id")
    Integer findActiveLinkByEmail(@Param("user_id") int userId);

    @Query("select ut.token from UserToken ut join ut.user u on ut.user = u.id" +
            " where ut.user.id=:user_id")
    String findTokenByUserId(@Param("user_id") int userId);

    @Query(value = "select ut.user_id from user_token ut where token = ?1", nativeQuery = true)
    Integer findUserMailByToken(@Param("token") String token);

    @Transactional
    @Modifying
    @Query("delete from UserToken ut where ut.user.id=:user_id")
    void deleteToken(@Param("user_id") int userId);

    @Query("select ut.dateCreationToken from UserToken ut join ut.user u on ut.user = u.id where u.id=:user_id")
    Date getTime(@Param("user_id") int userId);

    @Query("select count(ut) from UserToken ut join ut.user u on ut.user = u.id" +
            " where ut.user.id=:user_id")
    Integer amountToken(@Param("user_id") int userId);

}
