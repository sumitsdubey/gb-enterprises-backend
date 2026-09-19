package in.hexarise.gb_enterprises.crm.repository;

import in.hexarise.gb_enterprises.crm.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByConsumerId(Long consumerId, Pageable pageable);
}
