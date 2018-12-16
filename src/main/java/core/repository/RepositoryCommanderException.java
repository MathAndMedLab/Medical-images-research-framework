package core.repository;

public class RepositoryCommanderException extends Exception {
    public RepositoryCommanderException() {
    }

    public RepositoryCommanderException(String message) {
        super(message);
    }

    public RepositoryCommanderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryCommanderException(Throwable cause) {
        super(cause);
    }

    public RepositoryCommanderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
