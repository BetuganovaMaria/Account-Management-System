package ru.betuganova.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.betuganova.Entity.BankAccountEntity;
import ru.betuganova.Entity.UserEntity;

import java.util.List;

/**
 * Repository interface for managing bank accounts.
 */
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    @Query("SELECT b FROM BankAccountEntity b WHERE b.id = :accountId")
    BankAccountEntity findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT b FROM BankAccountEntity b WHERE b.userId = :userId")
    List<BankAccountEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT b FROM BankAccountEntity b WHERE b.id = :accountId AND b.userId = :userId")
    BankAccountEntity findByIdAndUserId(@Param("accountId") Long accountId, @Param("userId") Long userId);
}
