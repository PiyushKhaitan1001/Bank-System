package com.bank.server;

import com.sun.net.httpserver.HttpExchange;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);
        return sessionId;
    }

    public String getUsername(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        return sessions.get(sessionId);
    }

    public void invalidateSession(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }

    public String getSessionIdFromExchange(HttpExchange exchange) {
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        if (cookieHeaders == null) {
            return null;
        }
        for (String header : cookieHeaders) {
            String[] cookiePairs = header.split(";");
            for (String pair : cookiePairs) {
                String[] kv = pair.trim().split("=", 2);
                if (kv.length == 2) {
                    if (kv[0].trim().equals("session_id")) {
                        return kv[1].trim();
                    }
                }
            }
        }
        return null;
    }
}
