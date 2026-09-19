package in.hexarise.gb_enterprises.crm.repository;

import in.hexarise.gb_enterprises.crm.domain.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    @Query("""
        SELECT l FROM Lead l
        WHERE (cast(:name as string) IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', cast(:name as string), '%')))
          AND (cast(:mobile as string) IS NULL OR l.mobile LIKE CONCAT('%', cast(:mobile as string), '%'))
          AND (:valid IS NULL OR l.valid = :valid)
          AND (:alreadyInstalled IS NULL OR l.alreadyInstalled = :alreadyInstalled)
        """)
    Page<Lead> search(@Param("name") String name, @Param("mobile") String mobile,
                      @Param("valid") Boolean valid, @Param("alreadyInstalled") Boolean alreadyInstalled,
                      Pageable pageable);

    long countByValidTrue();
    java.util.List<Lead> findTop5ByOrderByCreatedAtDesc();
}
