package devracom.Mnemosyne.services;

import devracom.Mnemosyne.config.jwt.JwtService;
import devracom.Mnemosyne.models.Account;
import devracom.Mnemosyne.models.Role;
import devracom.Mnemosyne.models.dto.AuthRequest;
import devracom.Mnemosyne.models.dto.AuthResponse;
import devracom.Mnemosyne.models.dto.RegisterRequest;
import devracom.Mnemosyne.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Role role = new Role();
        role.setName("user");

        var account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        accountRepository.save(account);
        String jwtToken = jwtService.generateToken(account);

        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(account);

        return new AuthResponse(jwtToken);
    }
}
