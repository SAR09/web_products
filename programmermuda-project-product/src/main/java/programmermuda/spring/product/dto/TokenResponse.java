package programmermuda.spring.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("roles")
    private List<String> roles;

    public TokenResponse(String accessToken, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.username = username;
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
