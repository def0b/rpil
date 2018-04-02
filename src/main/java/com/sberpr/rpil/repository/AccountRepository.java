package com.sberpr.rpil.repository;

import com.sberpr.rpil.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;



public interface AccountRepository extends JpaRepository<AccountEntity,UUID>{
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findById(UUID uuid);
}
