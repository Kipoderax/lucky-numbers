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

    boolean existsByLogin(String login);

    @Query("select saldo from User where login=:login")
    Integer findSaldoByLogin(@Param("login") String login);

    @Transactional
    @Modifying
    @Query("update User set saldo=:saldo where login=:login")
    void updateUserSaldoByLogin(@Param("saldo") int saldo,
                                @Param("login") String login);

    @Query("select count(id) from User")
    Integer getAllRegisterUsers();

    @Query("select dateOfCreatedAccount from User where login=:login")
    Date findDateOfCreateAccountByLogin(@Param("login") String login);

    @Transactional
    @Modifying
    @Query("update User set lastLogin=:last_login where login=:login")
    void updateLastLoginByLogin(@Param("last_login") Date last_login,
                          @Param("login") String login);

    @Query("select lastLogin from User where login=:login")
    Date findLastLoginDateByLogin(@Param("login") String login);
}