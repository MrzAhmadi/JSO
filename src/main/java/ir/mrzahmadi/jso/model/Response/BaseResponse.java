package ir.mrzahmadi.jso.model.Response;

public class BaseResponse {

    String msg = "Successful";

    public BaseResponse() {
    }

    public BaseResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
