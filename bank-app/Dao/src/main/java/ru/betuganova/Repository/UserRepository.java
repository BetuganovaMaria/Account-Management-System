package ru.betuganova.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.betuganova.Entity.UserEntity;

import java.util.List;

/**
 * Repository interface for managing users and friendships.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);

    @Query("SELECT f FROM UserEntity u JOIN u.friends f WHERE u.login = :login")
    List<UserEntity> findFriendsByUserLogin(@Param("login") String login);
}
