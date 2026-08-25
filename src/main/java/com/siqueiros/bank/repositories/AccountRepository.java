package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    @Query("SELECT a FROM Account a JOIN FETCH a.user JOIN FETCH a.typeAccount " +
            "WHERE a.deletedAt IS NULL")
    List<Account> findAllActiveWithRelations();

    @Query("SELECT a FROM Account a JOIN FETCH a.user JOIN FETCH a.typeAccount " +
            "WHERE a.id = :accountId AND a.deletedAt IS NULL")
    Optional<Account> findActiveByIdWithRelations(@Param("accountId") Long accountId);

    boolean existsActiveByUserIdAndTypeAccountId(Long userId, Long typeAccountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a " +
            "WHERE a.id = :id AND a.deletedAt IS NULL")
    Optional<Account> findActiveByIdWithRowLevelLocking(@Param("id") Long id);
}
