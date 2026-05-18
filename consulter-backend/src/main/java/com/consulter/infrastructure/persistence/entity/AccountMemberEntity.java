package com.consulter.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMemberEntity {

    @EmbeddedId
    private AccountMemberId id;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;
}
