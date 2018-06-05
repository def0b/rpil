package com.sberpr.rpil.repository;

import com.sberpr.rpil.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by defabey on 24-Mar-18.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
/*
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findById(UUID uuid);
*/
}
