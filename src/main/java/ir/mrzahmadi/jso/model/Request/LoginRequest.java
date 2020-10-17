package ir.mrzahmadi.jso.model.Request;

public class LoginRequest {

    private String phoneNumber;

    public LoginRequest() {
    }

    public LoginRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
