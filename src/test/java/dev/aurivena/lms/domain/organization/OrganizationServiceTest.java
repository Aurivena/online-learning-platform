package dev.aurivena.lms.domain.organization;

import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.account.AccountService;
import dev.aurivena.lms.domain.organization.dto.CreateOrganizationRequest;
import dev.aurivena.lms.domain.organization.dto.OrganizationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private OrganizationMapper organizationMapper;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private OrganizationService organizationService;

    @Test
    void create_Success() {
        // Arrange
        CreateOrganizationRequest request = new CreateOrganizationRequest("Title", "TAG", "Desc");
        String ownerEmail = "test@example.com";
        Account owner = new Account();
        Organization organization = new Organization();
        OrganizationResponse expectedResponse = new OrganizationResponse(1L, "Title", "Desc", "TAG", null, new ArrayList<>(), null);

        when(accountService.getAccountByEmail(ownerEmail)).thenReturn(owner);
        when(organizationMapper.toEntity(request)).thenReturn(organization);
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponse(organization)).thenReturn(expectedResponse);

        // Act
        OrganizationResponse result = organizationService.create(request, ownerEmail);

        // Assert
        assertEquals(expectedResponse, result);
        verify(organizationRepository).save(organization);
        assertEquals(owner, organization.getOwner());
    }

    @Test
    void search_Success() {
        // Arrange
        String accountID = "1";
        Pageable pageable = Pageable.unpaged();
        List<Organization> organizations = List.of(new Organization());
        Page<Organization> organizationPage = new PageImpl<>(organizations);
        OrganizationResponse response = new OrganizationResponse(1L, "Title", "Desc", "TAG", null, new ArrayList<>(), null);

        when(organizationRepository.search(accountID, pageable)).thenReturn(organizationPage);
        when(organizationMapper.toResponse(any())).thenReturn(response);

        // Act
        Page<OrganizationResponse> result = organizationService.search(accountID, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().get(0));
    }

    @Test
    void getByTag_Success() {
        // Arrange
        String tag = "TAG";
        Organization organization = new Organization();
        OrganizationResponse expectedResponse = new OrganizationResponse(1L, "Title", "Desc", "TAG", null, new ArrayList<>(), null);

        when(organizationRepository.findByTag(tag)).thenReturn(Optional.of(organization));
        when(organizationMapper.toResponse(organization)).thenReturn(expectedResponse);

        // Act
        OrganizationResponse result = organizationService.getByTag(tag);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void getByTag_NotFound() {
        // Arrange
        String tag = "TAG";
        when(organizationRepository.findByTag(tag)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> organizationService.getByTag(tag));
    }
}
