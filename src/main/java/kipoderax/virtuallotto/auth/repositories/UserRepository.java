package kipoderax.virtuallotto.auth.repositories;

import kipoderax.virtuallotto.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //wyszukaj użytkownika po loginie
    Optional<User> findByLogin(String login);

    @Query("select username from User")
    String getAllLogin();

    //sprawdź czy użytkownik istnieje po loginie
    boolean existsByLogin(String login);

    //pobiera saldo do widoku na koncie użytkownika
    @Query("select saldo from User where login=:login")
    Integer findSaldoByLogin(@Param("login") String login);

    //Aktualizuje konto użytkownika na podstawie trafień w grze
    @Transactional
    @Modifying
    @Query("update User set saldo=:saldo where login=:login")
    void updateUserSaldoByLogin(@Param("saldo") int saldo,
                                @Param("login") String login);

    //Zlicza wszystkich aktualnie zarejestrowanych użytkowników
    //todo podmienić na zapytanie które zlicza ilość rekordów w tabeli user,
    // na wypadek wdrożenia opcji kasowania swojego konta
    @Query("select count(id) from User")
    Integer getCountPlayers();
}