package PhotoInterface.Exceptions;

public class WrongInputException extends Exception{



    public WrongInputException(String message, Throwable cause) {
        super(message, cause);
    }


    public WrongInputException(String message) {
        super(message);
    }
}
