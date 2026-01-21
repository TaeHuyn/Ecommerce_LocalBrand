package fpt.com.ecommerce.auth.service;

import fpt.com.ecommerce.auth.entity.Account;
import fpt.com.ecommerce.auth.repository.AccountRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@NullMarked
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository repository;
    private final PasswordEncoder encoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    public DataInitializer(AccountRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repository.findByUsername(adminUsername).isEmpty()) {
            Account admin = new Account();
            admin.setUsername(adminUsername);
            admin.setPassword(encoder.encode(adminPassword));
            admin.setRoles("ROLE_ADMIN");
            repository.save(admin);
        }
    }
}
