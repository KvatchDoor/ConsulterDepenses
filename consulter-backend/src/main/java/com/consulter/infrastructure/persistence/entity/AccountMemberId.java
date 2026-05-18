package com.consulter.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMemberId implements Serializable {

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "user_id")
    private UUID userId;
}
