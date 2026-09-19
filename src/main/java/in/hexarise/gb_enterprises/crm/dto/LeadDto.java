package in.hexarise.gb_enterprises.crm.dto;

import in.hexarise.gb_enterprises.crm.domain.Lead;
import in.hexarise.gb_enterprises.crm.dto.UserDto.PageResponse;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class LeadDto {

    public record CreateLeadRequest(
        @NotBlank String name, @NotBlank String mobile, String address,
        String load, Boolean valid, Boolean alreadyInstalled) {}

    public record UpdateLeadRequest(Boolean valid, Boolean alreadyInstalled, String name, String mobile, String address, String load) {}

    public record LeadResponse(
        Long id, String name, String mobile, String address, String load,
        boolean valid, boolean alreadyInstalled,
        String createdBy, Instant createdAt, Instant updatedAt) {

        public static LeadResponse from(Lead l) {
            return new LeadResponse(
                l.getId(), l.getName(), l.getMobile(), l.getAddress(), l.getLoad(),
                l.isValid(), l.isAlreadyInstalled(),
                l.getCreatedBy() != null ? l.getCreatedBy().getName() : null,
                l.getCreatedAt(), l.getUpdatedAt());
        }
    }
}
