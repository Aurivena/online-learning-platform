package dev.aurivena.lms.domain.organization;

import dev.aurivena.lms.domain.organization.dto.CreateOrganizationRequest;
import dev.aurivena.lms.domain.organization.dto.OrganizationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization", description = "Работа с организацией")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping(produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<OrganizationResponse> createOrganization(@Valid @RequestBody CreateOrganizationRequest request, @AuthenticationPrincipal String email) {
        return dev.aurivena.lms.common.api.ApiResponse.success(organizationService.create(request, email));
    }

    @Operation(summary = "Поиск организаций")
    @GetMapping(produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<Page<OrganizationResponse>> search(@RequestParam(required = false) String login, @RequestParam(required = false) String tag, @ParameterObject Pageable pageable
    ) {
        return dev.aurivena.lms.common.api.ApiResponse.success(organizationService.search(login, tag, pageable));
    }
}
