package features.repositoryaccessors;

public class AlgorithmExecutionException extends RuntimeException {
    public AlgorithmExecutionException() {
    }

    public AlgorithmExecutionException(String message) {
        super(message);
    }

    public AlgorithmExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlgorithmExecutionException(Throwable cause) {
        super(cause);
    }

    public AlgorithmExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
