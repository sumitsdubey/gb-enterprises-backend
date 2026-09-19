package in.hexarise.gb_enterprises.crm.dto;

import in.hexarise.gb_enterprises.crm.dto.ConsumerDto.ConsumerResponse;
import in.hexarise.gb_enterprises.crm.dto.EnquiryDto.EnquiryResponse;
import in.hexarise.gb_enterprises.crm.dto.LeadDto.LeadResponse;

import java.util.List;
import java.util.Map;

public class DashboardDto {

    public record DashboardStats(
            long totalLeads,
            long validLeads,
            long totalConsumers,
            long totalEnquiries,
            long totalUsers,
            long totalFeedbacks
    ) {}

    public record ConsumerAggregations(
            Map<String, Long> consumersByStatus,
            Map<String, Long> consumersBySystemType
    ) {}

    public record RecentActivity(
            List<LeadResponse> recentLeads,
            List<ConsumerResponse> recentConsumers,
            List<EnquiryResponse> recentEnquiries
    ) {}

    public record DashboardResponse(
            DashboardStats stats,
            ConsumerAggregations consumerAggregations,
            RecentActivity recentActivity
    ) {}
}
