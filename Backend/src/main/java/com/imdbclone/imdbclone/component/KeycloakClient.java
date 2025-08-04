package com.imdbclone.imdbclone.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakClient {
    private String realmName;
    private String baseUrl;
    private String clientId;

    // Getters and setters required for @ConfigurationProperties
    public String getRealmName() { return realmName; }
    public void setRealmName(String realmName) { this.realmName = realmName; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
}
