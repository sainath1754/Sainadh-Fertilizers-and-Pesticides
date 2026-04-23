package com.sainadh.fertilizers.init;

import com.sainadh.fertilizers.model.Product;
import com.sainadh.fertilizers.model.User;
import com.sainadh.fertilizers.repository.ProductRepository;
import com.sainadh.fertilizers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Seed Admin User ───────────────────────────────────────
        if (!userRepository.existsByUsername("admin")) {

            User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Sainadh@1754"))  // BCrypt-hashed
                .email("sainadh1754@gmail.com")
                .fullName("Sainadh Admin")
                .role("ADMIN")
                .enabled(true)
                .locked(false)
                .failedAttempts(0)
                .build();

            userRepository.save(admin);

            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.info("✅  Admin account created successfully");
            log.info("    Username  :  admin");
            log.info("    Email     :  sainadh1754@gmail.com");
            log.info("    Password  :  Sainadh@1754");
            log.info("    Role      :  ADMIN");
            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        } else {
            log.info("ℹ️   Admin already exists in DB — skipping creation.");
        }

        // ── Seed Products ─────────────────────────────────────────
        if (productRepository.count() == 0) {

            // Fertilizers
            productRepository.save(Product.builder()
                .name("Urea (46-0-0)")
                .description("High nitrogen fertilizer for leafy growth. Suitable for rice, wheat, and vegetable crops.")
                .price(350.00)
                .category("FERTILIZER")
                .imageUrl("🌾")
                .stock(200)
                .build());

            productRepository.save(Product.builder()
                .name("DAP (18-46-0)")
                .description("Diammonium Phosphate — excellent for root development and early plant growth.")
                .price(1350.00)
                .category("FERTILIZER")
                .imageUrl("🌱")
                .stock(150)
                .build());

            productRepository.save(Product.builder()
                .name("MOP (0-0-60)")
                .description("Muriate of Potash — boosts fruit quality, disease resistance, and water retention.")
                .price(900.00)
                .category("FERTILIZER")
                .imageUrl("🥬")
                .stock(120)
                .build());

            productRepository.save(Product.builder()
                .name("NPK 20-20-20")
                .description("Balanced fertilizer with equal parts nitrogen, phosphorus, and potassium for all crops.")
                .price(750.00)
                .category("FERTILIZER")
                .imageUrl("🌿")
                .stock(180)
                .build());

            productRepository.save(Product.builder()
                .name("Super Phosphate")
                .description("Single Super Phosphate for phosphorus-deficient soils. Great for legumes and oilseeds.")
                .price(480.00)
                .category("FERTILIZER")
                .imageUrl("🍀")
                .stock(100)
                .build());

            productRepository.save(Product.builder()
                .name("Organic Vermicompost")
                .description("100% natural organic compost from earthworm processing. Enriches soil health.")
                .price(250.00)
                .category("FERTILIZER")
                .imageUrl("🪱")
                .stock(300)
                .build());

            // Pesticides
            productRepository.save(Product.builder()
                .name("Chlorpyrifos 20% EC")
                .description("Broad-spectrum insecticide for termites, borers, and soil insects in cotton and vegetables.")
                .price(420.00)
                .category("PESTICIDE")
                .imageUrl("🧪")
                .stock(90)
                .build());

            productRepository.save(Product.builder()
                .name("Imidacloprid 17.8% SL")
                .description("Systemic insecticide effective against aphids, whiteflies, and jassids.")
                .price(580.00)
                .category("PESTICIDE")
                .imageUrl("🔬")
                .stock(110)
                .build());

            productRepository.save(Product.builder()
                .name("Mancozeb 75% WP")
                .description("Contact fungicide for blight, rust, and leaf spot diseases in potato, tomato, and grapes.")
                .price(320.00)
                .category("PESTICIDE")
                .imageUrl("🛡️")
                .stock(140)
                .build());

            productRepository.save(Product.builder()
                .name("Glyphosate 41% SL")
                .description("Non-selective herbicide for broad-leaf weeds and grasses. Use before planting.")
                .price(650.00)
                .category("PESTICIDE")
                .imageUrl("🌾")
                .stock(80)
                .build());

            productRepository.save(Product.builder()
                .name("Neem Oil Extract")
                .description("Organic bio-pesticide from neem seeds. Controls sucking pests and fungal infections.")
                .price(280.00)
                .category("PESTICIDE")
                .imageUrl("🌳")
                .stock(200)
                .build());

            productRepository.save(Product.builder()
                .name("Cypermethrin 25% EC")
                .description("Synthetic pyrethroid insecticide for bollworms, caterpillars, and pod borers.")
                .price(390.00)
                .category("PESTICIDE")
                .imageUrl("⚗️")
                .stock(130)
                .build());

            log.info("✅  Seeded 12 products (6 fertilizers + 6 pesticides)");
        } else {
            log.info("ℹ️   Products already exist in DB — skipping seed.");
        }
    }
}