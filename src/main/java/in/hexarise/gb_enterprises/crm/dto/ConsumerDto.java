package in.hexarise.gb_enterprises.crm.dto;

import in.hexarise.gb_enterprises.crm.domain.Consumer;
import in.hexarise.gb_enterprises.crm.domain.Remark;
import in.hexarise.gb_enterprises.crm.domain.Feedback;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class ConsumerDto {

    public record CreateConsumerRequest(
        @NotNull String name, @NotNull String mobile, String address,
        Consumer.Status status, Double lat, Double lng,
        String googleMapUrl, Double load, Consumer.SystemType type,
        Consumer.NextAction nextAction, java.time.LocalDate expectedActionDate) {}

    public record UpdateConsumerRequest(
        String name, String mobile, String address,
        Consumer.Status status, Double lat, Double lng,
        String googleMapUrl, Double load, Consumer.SystemType type,
        Consumer.NextAction nextAction, java.time.LocalDate expectedActionDate) {}

    public record AddRemarkRequest(String text) {}

    public record FeedbackRequest(String text, int rating) {}

    public record RemarkResponse(Long id, String text, String createdBy, Instant createdAt) {
        public static RemarkResponse from(Remark r) {
            return new RemarkResponse(r.getId(), r.getText(),
                r.getCreatedBy() != null ? r.getCreatedBy().getName() : null, r.getCreatedAt());
        }
    }

    public record FeedbackResponse(Long id, String text, int rating, String createdBy, Instant createdAt) {
        public static FeedbackResponse from(Feedback f) {
            return new FeedbackResponse(f.getId(), f.getText(), f.getRating(),
                f.getCreatedBy() != null ? f.getCreatedBy().getName() : null, f.getCreatedAt());
        }
    }

    public record ConsumerResponse(
        Long id, String name, String mobile, String address,
        Consumer.Status status, Double lat, Double lng, String googleMapUrl,
        Double load, Consumer.SystemType type,
        Consumer.NextAction nextAction, java.time.LocalDate expectedActionDate,
        List<String> images, List<RemarkResponse> remarks,
        String createdBy, String updatedBy, Instant createdAt, Instant updatedAt) {

        public static ConsumerResponse from(Consumer c) {
            return new ConsumerResponse(
                c.getId(),
                c.getName(), c.getMobile(), c.getAddress(),
                c.getStatus(), c.getLat(), c.getLng(), c.getGoogleMapUrl(), c.getLoad(), c.getType(),
                c.getNextAction(), c.getExpectedActionDate(),
                c.getImages().stream().map(i -> i.getUrl()).toList(),
                c.getRemarks().stream().map(RemarkResponse::from).toList(),
                c.getCreatedBy() != null ? c.getCreatedBy().getName() : null,
                c.getUpdatedBy() != null ? c.getUpdatedBy().getName() : null,
                c.getCreatedAt(), c.getUpdatedAt());
        }
    }
}
