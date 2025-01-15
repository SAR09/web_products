package programmermuda.product.auth.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message){
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
