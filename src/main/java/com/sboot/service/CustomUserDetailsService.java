package com.sboot.service;


import com.sboot.entity.User;
import com.sboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.sboot.entity.User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String roleName = user.getRole().getRoleName();  // e.g., "Admin"

        return new org.springframework.security.core.userdetails.User(
            user.getUserEmail(),
            user.getUserPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
        );
    }

}
