package ir.mrzahmadi.jso.model.Request;

public class VerifyOTPRequest {

    private String phoneNumber;
    private String otp;

    public VerifyOTPRequest() {
    }

    public VerifyOTPRequest(String phoneNumber, String otp) {
        this.phoneNumber = phoneNumber;
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOtp() {
        return otp;
    }
}
