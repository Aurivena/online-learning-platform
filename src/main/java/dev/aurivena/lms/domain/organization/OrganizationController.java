package dev.aurivena.lms.domain.organization;

import dev.aurivena.lms.common.api.Spond;
import dev.aurivena.lms.domain.organization.dto.CreateOrganizationRequest;
import dev.aurivena.lms.domain.organization.dto.OrganizationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization", description = "Работа с организацией")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(produces = "application/json")
    public Spond<OrganizationResponse> createOrganization(@Valid @RequestBody CreateOrganizationRequest request, @AuthenticationPrincipal String email) {
        return Spond.success(organizationService.create(request, email));
    }

    @Operation(summary = "Поиск организаций")
    @GetMapping(produces = "application/json")
    public Spond<Page<OrganizationResponse>> search(@RequestParam(required = false) String accountID, Pageable pageable) {
        return Spond.success(organizationService.search(accountID,pageable));
    }

    @Operation(summary = "Получить организацию по тегу")
    @GetMapping("/{tag}")
    public Spond<OrganizationResponse> getByTag(@PathVariable String tag) {
        return Spond.success(organizationService.getByTag(tag));
    }
}
