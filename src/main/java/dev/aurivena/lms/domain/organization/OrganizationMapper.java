package dev.aurivena.lms.domain.organization;

import dev.aurivena.lms.domain.account.AccountMapper;
import dev.aurivena.lms.domain.organization.dto.CreateOrganizationRequest;
import dev.aurivena.lms.domain.organization.dto.OrganizationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AccountMapper.class})
public interface OrganizationMapper {
    OrganizationResponse toResponse(Organization org);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tag", expression = "java(request.tag().toUpperCase())")
    Organization toEntity(CreateOrganizationRequest request);
}
