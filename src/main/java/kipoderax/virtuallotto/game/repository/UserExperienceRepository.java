package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.UserExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExperienceRepository extends JpaRepository<UserExperience, Integer> {

    @Query("select ue.level from UserExperience ue join ue.user u on ue.user = u.id where u.login=:login")
    Integer findLevelByLogin(@Param("login") String login);

    @Query("select ue.experience from UserExperience ue join ue.user u on ue.user = u.id where u.login=:login")
    Integer findExpByLogin(@Param("login") String login);

}
