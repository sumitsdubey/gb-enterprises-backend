package in.hexarise.gb_enterprises.crm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity @Table(name = "enquiries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Enquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 15)
    private String mobile;

    private String city;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConsumerType consumerType;

    private Double monthlyBill;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private PreferredSystem preferredSystem;

    @CreationTimestamp private Instant createdAt;

    public enum ConsumerType { HOME_OWNER, BUSINESS, COMMERCIAL }
    public enum PreferredSystem { ONGRID, OFFGRID, HYBRID, NOT_SURE }
}
