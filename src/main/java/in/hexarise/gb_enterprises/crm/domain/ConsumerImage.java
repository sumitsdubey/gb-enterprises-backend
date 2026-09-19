package in.hexarise.gb_enterprises.crm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity @Table(name = "consumer_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConsumerImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Column(nullable = false)
    private String url;

    @CreationTimestamp private Instant createdAt;
}
