package shiver.me.timbers.aws.lambda.soap.stub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyFailure {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }
}
