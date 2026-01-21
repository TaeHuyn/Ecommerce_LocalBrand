package fpt.com.ecommerce.auth.service;

import fpt.com.ecommerce.auth.entity.Account;
import fpt.com.ecommerce.auth.repository.AccountRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@NullMarked
public class AccountService implements UserDetailsService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account admin = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));

        var authorities = Arrays.stream(admin.getRoles().split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
