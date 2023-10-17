package com.nancho313.loqui.auth.persistence.service;

import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import com.nancho313.loqui.auth.persistence.client.mongodb.document.AuthUserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserAuthenticator implements UserDetailsService {

    private final AuthUserMongodbDAO authUserDao;

    private final PasswordEncoder loquiPasswordEncoder;

    public void storeBasicCredentials(String id, String username, String password, String email) {

        authUserDao.save(new AuthUserDocument(id, username, loquiPasswordEncoder.encode(password), email, LocalDateTime.now()));
    }

    public boolean existsByUsername(String username) {
        return authUserDao.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return authUserDao.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return authUserDao.findByUsername(username).map(this::toUserDetails).orElse(null);
    }

    private UserDetails toUserDetails(AuthUserDocument authUserDocument) {

        return User.builder()
                .username(authUserDocument.username())
                .password(authUserDocument.password())
                .passwordEncoder(loquiPasswordEncoder::encode)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
