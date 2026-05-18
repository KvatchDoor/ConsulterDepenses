package com.consulter.infrastructure.persistence.jpa;

import com.consulter.infrastructure.persistence.entity.AccountMemberEntity;
import com.consulter.infrastructure.persistence.entity.AccountMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

public interface AccountMemberJpaRepository extends JpaRepository<AccountMemberEntity, AccountMemberId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AccountMemberEntity m WHERE m.id.accountId = :accountId AND m.id.userId = :userId")
    void deleteByAccountIdAndUserId(@Param("accountId") UUID accountId, @Param("userId") UUID userId);
}
