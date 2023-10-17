package com.nancho313.loqui.auth.persistence.client.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("authuser")
public record AuthUserDocument(@Id String id, String username, String password, String email, LocalDateTime creationDate) {
}
