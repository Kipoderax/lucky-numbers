package kipoderax.virtuallotto.game.repository;

import kipoderax.virtuallotto.game.entity.UserExperience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserExperienceRepository extends JpaRepository<UserExperience, Integer> {

    @Query("select ue.level from UserExperience ue join ue.user u on ue.user = u.id where u.username=:username")
    Integer findLevelByLogin(@Param("username") String username);

    @Query("select ue.experience from UserExperience ue join ue.user u on ue.user = u.id where u.username=:username")
    Integer findExpByLogin(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("update UserExperience ue set ue.experience=:experience where ue.user in " +
            "(select u.id from User u where ue.user = u.id and u.id=:id)")
    void updateExperienceById(@Param("id") int id,
                              @Param("experience") int experience);

    @Transactional
    @Modifying
    @Query("update UserExperience ue set ue.level=:level where ue.user in " +
            "(select u.id from User u where ue.user = u.id and u.id=:id)")
    void updateLevelById(@Param("id") int id,
                         @Param("level") int level);
}
