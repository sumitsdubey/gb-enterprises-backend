package in.hexarise.gb_enterprises.crm.repository;

import in.hexarise.gb_enterprises.crm.domain.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    List<Enquiry> findTop5ByOrderByCreatedAtDesc();
}
