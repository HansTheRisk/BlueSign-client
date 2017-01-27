package project.bluesign.domain.message;

public class Message {

    private String message;
    private String responseCode;

    public Message() {

    }

    public Message(String message, String responseCode) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
