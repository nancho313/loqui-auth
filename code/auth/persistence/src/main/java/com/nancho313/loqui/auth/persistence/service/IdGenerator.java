package com.nancho313.loqui.auth.persistence.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    public String generateId() {
        return new ObjectId().toHexString();
    }
}
