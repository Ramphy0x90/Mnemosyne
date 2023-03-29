package devracom.Mnemosyne.config;

import devracom.Mnemosyne.models.Role;
import devracom.Mnemosyne.repositories.AccountRepository;
import devracom.Mnemosyne.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            // Default roles reference string
            List<String> defaultRoles = List.of("ROLE_ADMIN", "ROLE_USER");

            if(roleRepository.count() == 0) {
                // Create default roles
                for(String role: defaultRoles) {
                    roleRepository.save(Role.builder().name(role).build());
                }
            }
        };
    }
}
