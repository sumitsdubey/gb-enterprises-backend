package in.hexarise.gb_enterprises.crm.dto;

import in.hexarise.gb_enterprises.crm.domain.Enquiry;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class EnquiryDto {

    public record EnquiryRequest(
        @NotBlank String name, @NotBlank String mobile, String city,
        Enquiry.ConsumerType consumerType, Double monthlyBill, Enquiry.PreferredSystem preferredSystem) {}

    public record UpdateEnquiryRequest(
        String name, String mobile, String city,
        Enquiry.ConsumerType consumerType, Double monthlyBill, Enquiry.PreferredSystem preferredSystem) {}

    public record EnquiryResponse(
        Long id, String name, String mobile, String city,
        Enquiry.ConsumerType consumerType, Double monthlyBill,
        Enquiry.PreferredSystem preferredSystem, Instant createdAt) {

        public static EnquiryResponse from(Enquiry e) {
            return new EnquiryResponse(e.getId(), e.getName(), e.getMobile(), e.getCity(),
                e.getConsumerType(), e.getMonthlyBill(), e.getPreferredSystem(), e.getCreatedAt());
        }
    }
}
