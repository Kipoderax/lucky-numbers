package kipoderax.virtuallotto.auth.repositories;

import kipoderax.virtuallotto.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);

    @Query(value = "select id from user order by id desc limit 1", nativeQuery = true)
    Integer findMaxId();

    boolean existsByUsername(String username);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    @Query("select username from User where id=:user_id")
    String findUsernameByUserId(@Param("user_id") int userId);

    @Query("select email from User where id=:user_id")
    String findEmailByUserId(@Param("user_id") int userId);

    @Query("select saldo from User where id=:user_id")
    Integer findSaldoByLogin(@Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query("update User set saldo=:saldo where id=:user_id")
    void updateUserSaldoByLogin(@Param("saldo") int saldo,
                                @Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query("update User u set u.password=:password where u.id=:user_id")
    void updateUserPassword(@Param("password") String password,
                            @Param("user_id") int userId);

    @Query("select count(id) from User")
    Integer getAllRegisterUsers();

    @Query("select dateOfCreatedAccount from User where id=:user_id")
    Date findDateOfCreateAccountByUserId(@Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query("update User set lastLogin=:last_login where id=:user_id")
    void updateLastLoginByLogin(@Param("last_login") Date lastLogin,
                          @Param("user_id") int userId);

    @Query("select lastLogin from User where id=:user_id")
    Date findLastLoginDateByUserId(@Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query("delete from User where id=:user_id")
    void deleteAccountByUserId(@Param("user_id") int userId);
}