package in.hexarise.gb_enterprises.crm.repository;

import in.hexarise.gb_enterprises.crm.domain.Remark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RemarkRepository extends JpaRepository<Remark, Long> {
    List<Remark> findByConsumerIdOrderByCreatedAtDesc(Long consumerId);
}
