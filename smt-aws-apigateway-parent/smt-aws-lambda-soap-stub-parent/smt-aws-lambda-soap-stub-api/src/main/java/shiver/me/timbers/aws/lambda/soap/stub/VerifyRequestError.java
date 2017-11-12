package shiver.me.timbers.aws.lambda.soap.stub;

class VerifyRequestError extends AssertionError {
    VerifyRequestError(String message) {
        super(message);
    }
}
