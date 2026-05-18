package com.consulter.infrastructure.persistence.jpa;

import com.consulter.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

    @Query("""
        SELECT a FROM AccountEntity a
        WHERE a.ownerId = :userId
           OR EXISTS (
               SELECT m FROM AccountMemberEntity m
               WHERE m.id.accountId = a.id AND m.id.userId = :userId
           )
        """)
    List<AccountEntity> findAccessibleByUserId(@Param("userId") UUID userId);
}
