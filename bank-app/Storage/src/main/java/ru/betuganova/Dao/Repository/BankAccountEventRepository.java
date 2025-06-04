package ru.betuganova.Dao.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.betuganova.Dao.Entity.BankAccountEvent;

public interface BankAccountEventRepository extends JpaRepository<BankAccountEvent, Long> {
}
