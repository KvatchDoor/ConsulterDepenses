package com.consulter.infrastructure.persistence.mapper;

import com.consulter.domain.model.Account;
import com.consulter.infrastructure.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toDomain(AccountEntity entity);
    AccountEntity toEntity(Account account);
}
