package in.hexarise.gb_enterprises.crm.repository;

import in.hexarise.gb_enterprises.crm.domain.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    @Query("""
        SELECT c FROM Consumer c
        WHERE (cast(:name as string) IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', cast(:name as string), '%')))
          AND (cast(:mobile as string) IS NULL OR c.mobile LIKE CONCAT('%', cast(:mobile as string), '%'))
          AND (cast(:status as string) IS NULL OR CAST(c.status AS string) = cast(:status as string))
        ORDER BY c.expectedActionDate ASC NULLS LAST
        """)
    Page<Consumer> search(@Param("name") String name, @Param("mobile") String mobile, @Param("status") String status, Pageable pageable);

    java.util.List<Consumer> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT c.status, COUNT(c) FROM Consumer c GROUP BY c.status")
    java.util.List<Object[]> countConsumersByStatus();

    @Query("SELECT c.type, COUNT(c) FROM Consumer c WHERE c.type IS NOT NULL GROUP BY c.type")
    java.util.List<Object[]> countConsumersBySystemType();
}
