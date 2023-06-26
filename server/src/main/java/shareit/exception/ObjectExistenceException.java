package shareit.exception;

public class ObjectExistenceException extends RuntimeException {
    public ObjectExistenceException(String msg) {
        super(msg);
    }

    public ObjectExistenceException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
