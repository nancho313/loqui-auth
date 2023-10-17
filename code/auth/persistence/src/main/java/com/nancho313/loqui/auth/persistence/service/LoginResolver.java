package com.nancho313.loqui.auth.persistence.service;

import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class LoginResolver {

    private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "The given credentials are not valid.";
    private final AuthUserMongodbDAO authUserDao;

    private final PasswordEncoder loquiPasswordEncoder;

    public String login(String username, String password) {

        var optionalAuthUser = authUserDao.findByUsername(username);

        if (optionalAuthUser.isPresent()) {

            var authUser = optionalAuthUser.get();
            validatePassword(password, authUser.password());
            var key = Jwts.SIG.HS256.key().build();
            return Jwts.builder()
                    .subject(authUser.id())
                    .signWith(key)
                    .issuedAt(getIssuedAtDate())
                    .expiration(getExpirationDate())
                    .compact();
        } else {
            throw new BadCredentialsException(INVALID_CREDENTIALS_ERROR_MESSAGE);
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {

        if (!loquiPasswordEncoder.matches(rawPassword, encodedPassword)) {

            throw new BadCredentialsException(INVALID_CREDENTIALS_ERROR_MESSAGE);
        }
    }

    private Date getIssuedAtDate() {
        return new Date(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    private Date getExpirationDate() {

        return new Date(LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.UTC).toEpochMilli());
    }
}
