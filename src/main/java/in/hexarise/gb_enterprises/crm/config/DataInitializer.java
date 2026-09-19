package in.hexarise.gb_enterprises.crm.config;

import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Bean
    ApplicationRunner seedSuperAdmin() {
        return args -> {
            if (!userRepo.existsByEmail("sumitprsnl1@gmail.com")) {
                userRepo.save(User.builder()
                    .name("Sumit")
                    .email("sumitprsnl1@gmail.com")
                    .passwordHash(encoder.encode("Sumit@123"))
                    .role(User.Role.SUPER_ADMIN)
                    .active(true)
                    .build());
                log.info("✅ Super admin seeded: sumitprsnl1@gmail.com");
            }
        };
    }
}
