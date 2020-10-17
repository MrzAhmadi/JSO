package ir.mrzahmadi.jso.model.Response;

public class VerifyOTPResponse {

    private String token;

    public VerifyOTPResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
