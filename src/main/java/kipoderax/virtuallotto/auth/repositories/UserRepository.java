package kipoderax.virtuallotto.auth.repositories;

import kipoderax.virtuallotto.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);

    @Query(value = "select * from user where login = ?1", nativeQuery = true)
    List<User> findAllByLogin(String Login);

    @Query("select saldo from User where login=:login")
    Optional<Integer> findSaldoByLogin(@Param("login") String login);

    @Transactional
    @Modifying
    @Query("update User set saldo=:saldo where login=:login")
    void updateUserSaldoByLogin(@Param("saldo") int saldo,
                                @Param("login") String login);

//    Optional<UserSession> findSaldoByLogin(@Param("login"), )

}