package com.pcparts.shop.config;

import com.pcparts.shop.entity.Product;
import com.pcparts.shop.entity.Role;
import com.pcparts.shop.entity.Role.RoleName;
import com.pcparts.shop.entity.User;
import com.pcparts.shop.repository.ProductRepository;
import com.pcparts.shop.repository.RoleRepository;
import com.pcparts.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createRoles();
        createUsers();
        createProducts();
    }

    private void createRoles() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = Role.builder().name(roleName).build();
                log.info("Seeding role {}", roleName);
                return roleRepository.save(role);
            });
        }
    }

    private void createUsers() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow();
            User admin = User.builder()
                    .username("admin")
                    .email("admin@pcparts.shop")
                    .password(passwordEncoder.encode("Admin!234"))
                    .fullName("Gaming Parts Administrator")
                    .gender("Other")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .roles(Set.of(adminRole, userRole))
                    .build();
            userRepository.save(admin);
            log.info("Seeded admin user");
        }

        if (userRepository.findByUsername("salah").isEmpty()) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow();
            User user = User.builder()
                    .username("salah")
                    .email("salah@pcparts.shop")
                    .password(passwordEncoder.encode("User!234"))
                    .fullName("Salah Gamer")
                    .gender("Male")
                    .dateOfBirth(LocalDate.of(1995, 5, 5))
                    .roles(Set.of(userRole))
                    .build();
            userRepository.save(user);
            log.info("Seeded demo user");
        }
    }

    private void createProducts() {
        List<Map<String, String>> products = List.of(
                Map.of("sku", "FB-TIRE-20X4", "name", "Fat Tire 20x4.0", "price", "49.99", "description", "Durable 60 TPI all-terrain tire.", "image", "https://images.unsplash.com/photo-1515916717028-2b3c1c0e1a9e", "stock", "25"),
                Map.of("sku", "FB-TIRE-26X4", "name", "Fat Tire 26x4.0", "price", "59.99", "description", "Snow/sand optimized tread.", "image", "https://images.unsplash.com/photo-1541625602330-2277a4c46182", "stock", "30"),
                Map.of("sku", "FB-TUBE-20", "name", "Inner Tube 20x4.0", "price", "12.90", "description", "Butyl tube, Schrader valve.", "image", "https://images.unsplash.com/photo-1520975916090-3105956dac38", "stock", "60"),
                Map.of("sku", "FB-RIM-26", "name", "Alloy Rim 26\" Fat", "price", "89.00", "description", "Double-wall 80mm.", "image", "https://images.unsplash.com/photo-1595433707802-6b1d2a3a8a32", "stock", "40"),
                Map.of("sku", "FB-HUB-F", "name", "Front Hub 150mm", "price", "49.95", "description", "Sealed bearings, 32H.", "image", "https://images.unsplash.com/photo-1594007654729-407c2f2b4b0d", "stock", "35"),
                Map.of("sku", "FB-HUB-R", "name", "Rear Hub 197mm", "price", "69.95", "description", "HG freehub, 32H.", "image", "https://images.unsplash.com/photo-1520975693419-82a38c81b5b8", "stock", "30"),
                Map.of("sku", "FB-BRAKE-DB", "name", "Disc Brake Set", "price", "59.00", "description", "Mechanical, front+rear.", "image", "https://images.unsplash.com/photo-1516307365426-d3a3e4e9a3b1", "stock", "50"),
                Map.of("sku", "FB-ROTOR-180", "name", "Rotor 180mm", "price", "19.95", "description", "6-bolt stainless rotor.", "image", "https://images.unsplash.com/photo-1517649763962-0c623066013b", "stock", "80"),
                Map.of("sku", "FB-PAD-ORG", "name", "Organic Brake Pads", "price", "11.50", "description", "Quiet all-weather pads.", "image", "https://images.unsplash.com/photo-1517646287270-0c09a1fef54b", "stock", "100"),
                Map.of("sku", "FB-CHAIN-11", "name", "11-speed Chain", "price", "24.90", "description", "Anti-rust coating.", "image", "https://images.unsplash.com/photo-1485965120184-e220f721d03e", "stock", "75"),
                Map.of("sku", "FB-CAS-1142", "name", "Cassette 11-42T", "price", "54.90", "description", "Wide range steel cogs.", "image", "https://images.unsplash.com/photo-1492717009854-9d334fb1a841", "stock", "55"),
                Map.of("sku", "FB-CRANK-170", "name", "Crankset 170mm", "price", "79.00", "description", "Narrow-wide ring 32T.", "image", "https://images.unsplash.com/photo-1498654200943-1088dd4438ae", "stock", "40"),
                Map.of("sku", "FB-BAR-780", "name", "Handlebar 780mm", "price", "29.90", "description", "20mm rise alloy.", "image", "https://images.unsplash.com/photo-1517646287710-8c6d6d6d0312", "stock", "65"),
                Map.of("sku", "FB-STEM-50", "name", "Stem 50mm", "price", "24.90", "description", "31.8mm clamp.", "image", "https://images.unsplash.com/photo-1519681393784-d120267933ba", "stock", "85"),
                Map.of("sku", "FB-LIGHT-LED", "name", "LED Headlight", "price", "29.95", "description", "800 lm USB-C.", "image", "https://images.unsplash.com/photo-1509395176047-4a66953fd231", "stock", "90"),
                Map.of("sku", "FB-BIKE-ALP", "name", "Alpine Pro", "price", "1699.00", "description", "Alloy, 1x11, hydraulic brakes.", "image", "https://images.unsplash.com/photo-1460353581641-37baddab0fa2", "stock", "10"),
                Map.of("sku", "FB-BIKE-SNW", "name", "Snow Runner", "price", "1899.00", "description", "Carbon fork, 1x12.", "image", "https://images.unsplash.com/photo-1517646287474-0e9c3e9aa5d6", "stock", "8"),
                Map.of("sku", "FB-BIKE-DST", "name", "Desert Storm", "price", "1649.00", "description", "Sand-tuned tires, alloy.", "image", "https://images.unsplash.com/photo-1482192596544-9eb780fc7f66", "stock", "12"),
                Map.of("sku", "FB-BIKE-URB", "name", "Urban Commute", "price", "1299.00", "description", "Rack/mudguards, lights.", "image", "https://images.unsplash.com/photo-1485965120184-e220f721d03e", "stock", "15"),
                Map.of("sku", "FB-BIKE-TRK", "name", "Trail King", "price", "2099.00", "description", "Carbon frame, dropper.", "image", "https://images.unsplash.com/photo-1517646287710-8c6d6d6d0312", "stock", "7")
        );

        for (Map<String, String> productData : products) {
            String sku = productData.get("sku");
            if (productRepository.existsBySku(sku)) {
                continue;
            }
            Product product = Product.builder()
                    .sku(sku)
                    .name(productData.get("name"))
                    .price(new BigDecimal(productData.get("price")))
                    .description(productData.get("description"))
                    .imageUrl(productData.get("image"))
                    .stock(Integer.parseInt(productData.get("stock")))
                    .createdAt(OffsetDateTime.now())
                    .build();
            productRepository.save(product);
        }
    }
}


