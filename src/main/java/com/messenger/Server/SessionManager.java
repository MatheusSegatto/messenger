package com.messenger.Server;

import java.util.UUID;

public class SessionManager {
    public static String generateSessionId() {
        // Gerar um UUID aleatório
        return UUID.randomUUID().toString();
    }
}
