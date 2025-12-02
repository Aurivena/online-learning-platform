package dev.aurivena.lms.domain.organization;

import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.account.AccountService;
import dev.aurivena.lms.domain.organization.dto.CreateOrganizationRequest;
import dev.aurivena.lms.domain.organization.dto.OrganizationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    private final AccountService accountService;

    @Transactional
    public OrganizationResponse create(CreateOrganizationRequest createOrganizationRequest,String ownerEmail) {
        Account owner = accountService.getAccountByEmail(ownerEmail);

        Organization org = organizationMapper.toEntity(createOrganizationRequest);
        org.setOwner(owner);

        return organizationMapper.toResponse(organizationRepository.save(org));
    }

    @Transactional()
    public Page<OrganizationResponse> search(String login, String tag, Pageable pageable) {
        return organizationRepository.search(login, tag, pageable).map(organizationMapper::toResponse);
    }

    @Transactional()
    public OrganizationResponse getByTag(String tag) {
        Organization organization = organizationRepository.findByTag(tag)
                .orElseThrow(()-> new RuntimeException("Organization not found"));
        return organizationMapper.toResponse(organization);

    }
}
