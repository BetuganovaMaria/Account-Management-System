package ru.betuganova.Dao.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.betuganova.Dao.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsUserEntityByLogin(String login);

    UserEntity findByLogin(String login);

    UserEntity findById(long id);
}
