package in.hexarise.gb_enterprises.crm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "consumers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consumer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 15)
    private String mobile;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private Status status = Status.INTERESTED;

    private Double lat;
    private Double lng;
    private String googleMapUrl;

    private Double load; // kW

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private SystemType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NextAction nextAction;

    private java.time.LocalDate expectedActionDate;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConsumerImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Remark> remarks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @CreationTimestamp private Instant createdAt;
    @UpdateTimestamp  private Instant updatedAt;

    public enum Status {
        INTERESTED, NEED_VISIT, VISITED, APPLIED,
        LOAN_PROCESSING, DOCUMENTATION_PROCESSING, READY_TO_INSTALL, INSTALLED
    }

    public enum SystemType { ONGRID, OFFGRID, HYBRID }

    public enum NextAction { CALL, VISIT, DOCUMENT, INSTALL, COMPLETED }
}
