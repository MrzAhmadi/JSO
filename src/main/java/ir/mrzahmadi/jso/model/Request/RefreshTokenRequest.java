package ir.mrzahmadi.jso.model.Request;

public class RefreshTokenRequest {

    private String accessToken;
    private String refreshToken;

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
