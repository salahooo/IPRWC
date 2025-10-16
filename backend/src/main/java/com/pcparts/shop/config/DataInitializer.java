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
    Map.of("sku", "GPU-RTX4070", "name", "NVIDIA GeForce RTX 4070 12GB", "price", "679.00",
           "description", "Ray tracing, DLSS 3, 12GB GDDR6X. Ideal for 1440p gaming.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/GPU-RTX4070%20%E2%80%94%20NVIDIA%20GeForce%20RTX%204070%2012GB.jpg",
           "stock", "15"),
    Map.of("sku", "GPU-RX7900", "name", "AMD Radeon RX 7900 XT 20GB", "price", "799.00",
           "description", "20GB GDDR6, RDNA3 architecture, top-tier 4K performance.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/GPU-RX7900%20%E2%80%94%20AMD%20Radeon%20RX%207900%20XT%2020GB.jpg",
           "stock", "10"),
    Map.of("sku", "CPU-I7-13700K", "name", "Intel Core i7-13700K", "price", "429.00",
           "description", "16-core (8P + 8E), up to 5.4GHz. Excellent for gaming and productivity.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/CPU-I7-13700K%20%E2%80%94%20Intel%20Core%20i7-13700K.jpg",
           "stock", "20"),
    Map.of("sku", "CPU-R7-7800X3D", "name", "AMD Ryzen 7 7800X3D", "price", "449.00",
           "description", "8-core 5.0GHz 3D V-Cache CPU — unmatched gaming efficiency.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/CPU-R7-7800X3D%20%E2%80%94%20AMD%20Ryzen%207%207800X3D.jpg",
           "stock", "18"),
    Map.of("sku", "MB-ASUS-B650", "name", "ASUS TUF Gaming B650-PLUS WiFi", "price", "199.00",
           "description", "AM5 motherboard, DDR5, WiFi 6, PCIe 5.0 ready.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/MB-ASUS-B650%20%E2%80%94%20ASUS%20TUF%20Gaming%20B650-PLUS%20WiFi.jpg",
           "stock", "25"),
    Map.of("sku", "MB-MSI-Z790", "name", "MSI MAG Z790 Tomahawk WiFi", "price", "249.00",
           "description", "Intel LGA1700, DDR5, PCIe 5.0, dual M.2 slots.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/MB-MSI-Z790%20%E2%80%94%20MSI%20MAG%20Z790%20Tomahawk%20WiFi.jpg",
           "stock", "22"),
    Map.of("sku", "RAM-DDR5-32GB", "name", "Corsair Vengeance DDR5 32GB (2x16GB) 6000MHz", "price", "159.00",
           "description", "Dual-channel high-speed memory optimized for Intel/AMD gaming builds.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/RAM-DDR5-32GB%20%E2%80%94%20Corsair%20Vengeance%20DDR5%2032GB%20(2x16GB)%206000MHz.jpg",
           "stock", "40"),
    Map.of("sku", "RAM-DDR4-16GB", "name", "Kingston Fury Beast 16GB (2x8GB) 3600MHz", "price", "79.00",
           "description", "Reliable DDR4 kit for budget gaming PCs.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/RAM-DDR4-16GB%20%E2%80%94%20Kingston%20Fury%20Beast%2016GB%20(2x8GB)%203600MHz.jpg",
           "stock", "50"),
    Map.of("sku", "SSD-1TB-NVME", "name", "Samsung 990 PRO 1TB NVMe SSD", "price", "139.00",
           "description", "PCIe 4.0 SSD, blazing 7450MB/s read speed.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/SSD-1TB-NVME%20%E2%80%94%20Samsung%20990%20PRO%201TB%20NVMe%20SSD.jpg",
           "stock", "35"),
    Map.of("sku", "SSD-2TB-NVME", "name", "Crucial P5 Plus 2TB NVMe SSD", "price", "179.00",
           "description", "2TB Gen4 NVMe storage with fast write speeds.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/SSD-2TB-NVME%20%E2%80%94%20Crucial%20P5%20Plus%202TB%20NVMe%20SSD.jpg",
           "stock", "25"),
    Map.of("sku", "CASE-LIANLI-011", "name", "Lian Li O11 Dynamic EVO", "price", "159.00",
           "description", "Premium mid-tower case with glass panels and modular design.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/CASE-LIANLI-011%20%E2%80%94%20Lian%20Li%20O11%20Dynamic%20EVO.jpg",
           "stock", "15"),
    Map.of("sku", "CASE-NZXT-H7", "name", "NZXT H7 Flow", "price", "129.00",
           "description", "Airflow-optimized case with minimalist design.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/CASE-NZXT-H7%20%E2%80%94%20NZXT%20H7%20Flow.webp",
           "stock", "20"),
    Map.of("sku", "PSU-750W", "name", "Corsair RM750e 750W 80+ Gold", "price", "119.00",
           "description", "Fully modular PSU with silent fan and Japanese capacitors.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/PSU-750W%20%E2%80%94%20Corsair%20RM750e%20750W%2080+%20Gold.jpg",
           "stock", "30"),
    Map.of("sku", "PSU-850W", "name", "Seasonic Focus GX-850 80+ Gold", "price", "139.00",
           "description", "High-efficiency PSU perfect for RTX 40-series builds.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/PSU-850W%20%E2%80%94%20Seasonic%20Focus%20GX-850%2080+%20Gold.jpg",
           "stock", "25"),
    Map.of("sku", "COOLER-NOCTUA", "name", "Noctua NH-D15 Chromax Black", "price", "109.00",
           "description", "Legendary dual-tower CPU cooler — silent and powerful.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/COOLER-NOCTUA%20%E2%80%94%20Noctua%20NH-D15%20Chromax%20Black.avif",
           "stock", "20"),
    Map.of("sku", "COOLER-LIQUID", "name", "NZXT Kraken X73 RGB 360mm", "price", "189.00",
           "description", "AIO liquid cooler with RGB and performance monitoring.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/COOLER-LIQUID%20%E2%80%94%20NZXT%20Kraken%20X73%20RGB%20360mm.jpg",
           "stock", "18"),
    Map.of("sku", "GPU-RTX4090", "name", "NVIDIA GeForce RTX 4090 24GB", "price", "1849.00",
           "description", "Flagship GPU with Ada Lovelace architecture — extreme performance.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/GPU-RTX4090%20%E2%80%94%20NVIDIA%20GeForce%20RTX%204090%2024GB.jpg",
           "stock", "6"),
    Map.of("sku", "MB-ASROCK-B550", "name", "ASRock B550M Steel Legend", "price", "149.00",
           "description", "Micro-ATX AM4 board with robust power delivery and RGB headers.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/MB-ASROCK-B550%20%E2%80%94%20ASRock%20B550M%20Steel%20Legend.jpeg",
           "stock", "28"),
    Map.of("sku", "MONITOR-27", "name", "LG UltraGear 27\" 165Hz QHD", "price", "329.00",
           "description", "27-inch QHD IPS panel with 1ms response time.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/MONITOR-27%20%E2%80%94%20LG%20UltraGear%2027%20165Hz%20QHD.jpg",
           "stock", "14"),
    Map.of("sku", "PERIPHERAL-SET", "name", "Logitech G Pro Keyboard + G502 Mouse Combo", "price", "179.00",
           "description", "Mechanical keyboard and high-DPI gaming mouse.",
           "image", "https://raw.githubusercontent.com/salahooo/Images/main/PERIPHERAL-SET%20%E2%80%94%20Logitech%20G%20Pro%20Keyboard%20+%20G502%20Mouse%20Combo.jpg",
           "stock", "40")
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

    log.info("Seeded {} gaming PC products", products.size());

}

}
