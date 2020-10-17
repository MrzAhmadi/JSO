package ir.mrzahmadi.jso.model.Response;

public class DetailsResponse {

    private long id;
    private String phoneNumber ;

    public DetailsResponse(long id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
