package com.hariomclinic.security;

import com.hariomclinic.model.Admin;
import com.hariomclinic.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * CustomUserDetailsService - Implements Spring Security's UserDetailsService.
 *
 * Spring Security calls loadUserByUsername() during authentication to:
 *   1. Fetch user from DB by username
 *   2. Return UserDetails object (contains username, password, roles)
 *   3. Spring Security then compares the provided password with stored BCrypt hash
 *
 * UserDetails is a Spring Security interface - we use the built-in User class
 * which implements it, passing: username, hashed-password, list of authorities/roles.
 *
 * SimpleGrantedAuthority("ROLE_ADMIN") -> assigns ADMIN role to the doctor
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));

        return new User(
                admin.getUsername(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
