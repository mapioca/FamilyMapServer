package data;

public class DataAccessException extends Throwable {
    public DataAccessException(String message) {
        super(message);
    }

    DataAccessException(){
        super();
    }
}
