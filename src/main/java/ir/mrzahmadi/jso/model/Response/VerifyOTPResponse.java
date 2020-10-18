package ir.mrzahmadi.jso.model.Response;

public class VerifyOTPResponse {

    private String accessToken;
    private String refreshToken;

    public VerifyOTPResponse(String accessToken, String refreshToken) {
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
