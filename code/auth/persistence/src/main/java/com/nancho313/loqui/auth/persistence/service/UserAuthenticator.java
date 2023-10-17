package com.nancho313.loqui.auth.persistence.service;

import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import com.nancho313.loqui.auth.persistence.client.mongodb.document.AuthUserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserAuthenticator {

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
}