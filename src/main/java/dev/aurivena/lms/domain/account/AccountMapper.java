package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AccountResponse;
import dev.aurivena.lms.domain.account.dto.RegistrationRequest;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    AccountResponse toResponse(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Account toEntity(RegistrationRequest request);
}
