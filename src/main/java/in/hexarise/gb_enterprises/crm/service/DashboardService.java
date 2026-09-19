package in.hexarise.gb_enterprises.crm.service;

import in.hexarise.gb_enterprises.crm.dto.DashboardDto.*;
import in.hexarise.gb_enterprises.crm.dto.ConsumerDto.ConsumerResponse;
import in.hexarise.gb_enterprises.crm.dto.EnquiryDto.EnquiryResponse;
import in.hexarise.gb_enterprises.crm.dto.LeadDto.LeadResponse;
import in.hexarise.gb_enterprises.crm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final LeadRepository leadRepository;
    private final ConsumerRepository consumerRepository;
    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    public DashboardResponse getDashboardData() {
        // 1. Stats
        long totalLeads = leadRepository.count();
        long validLeads = leadRepository.countByValidTrue();
        long totalConsumers = consumerRepository.count();
        long totalEnquiries = enquiryRepository.count();
        long totalUsers = userRepository.count();
        long totalFeedbacks = feedbackRepository.count();

        DashboardStats stats = new DashboardStats(
                totalLeads, validLeads, totalConsumers, totalEnquiries, totalUsers, totalFeedbacks
        );

        // 2. Aggregations
        List<Object[]> statusCounts = consumerRepository.countConsumersByStatus();
        Map<String, Long> consumersByStatus = statusCounts.stream()
                .collect(Collectors.toMap(
                        obj -> obj[0] != null ? obj[0].toString() : "UNKNOWN",
                        obj -> (Long) obj[1]
                ));

        List<Object[]> typeCounts = consumerRepository.countConsumersBySystemType();
        Map<String, Long> consumersBySystemType = typeCounts.stream()
                .collect(Collectors.toMap(
                        obj -> obj[0] != null ? obj[0].toString() : "UNKNOWN",
                        obj -> (Long) obj[1]
                ));

        ConsumerAggregations aggregations = new ConsumerAggregations(consumersByStatus, consumersBySystemType);

        // 3. Recent Activity
        List<LeadResponse> recentLeads = leadRepository.findTop5ByOrderByCreatedAtDesc()
                .stream().map(LeadResponse::from).toList();

        List<ConsumerResponse> recentConsumers = consumerRepository.findTop5ByOrderByCreatedAtDesc()
                .stream().map(ConsumerResponse::from).toList();

        List<EnquiryResponse> recentEnquiries = enquiryRepository.findTop5ByOrderByCreatedAtDesc()
                .stream().map(EnquiryResponse::from).toList();

        RecentActivity recentActivity = new RecentActivity(recentLeads, recentConsumers, recentEnquiries);

        // 4. Build Response
        return new DashboardResponse(stats, aggregations, recentActivity);
    }
}
