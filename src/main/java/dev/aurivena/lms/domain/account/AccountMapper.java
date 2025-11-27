package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AuthResponse;
import dev.aurivena.lms.domain.account.dto.RegistrationRequest;
import dev.aurivena.lms.domain.account.dto.UpdateProfileRequest;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface AccountMapper {

    //Entity -> DTO
    AuthResponse toResponse(Account account);

    //Registration -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Account toEntity(RegistrationRequest request);

    //Update -> Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateAccountFromRequest(UpdateProfileRequest request, @MappingTarget Account account);
}
