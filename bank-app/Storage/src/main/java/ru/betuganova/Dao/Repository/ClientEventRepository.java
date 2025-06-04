package ru.betuganova.Dao.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.betuganova.Dao.Entity.ClientEvent;

public interface ClientEventRepository extends JpaRepository<ClientEvent, Long> {
}
