package shiver.me.timbers.aws.lambda.soap.stub;

public class Verifying {

    private String request;

    public Verifying() {
    }

    public Verifying(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Verifying verifying = (Verifying) o;

        return request != null ? request.equals(verifying.request) : verifying.request == null;
    }

    @Override
    public int hashCode() {
        return request != null ? request.hashCode() : 0;
    }
}
