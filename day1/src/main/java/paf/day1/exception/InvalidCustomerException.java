package paf.day1.exception;

public class InvalidCustomerException extends RuntimeException {
    
    public InvalidCustomerException() {
        super();
    }

    public InvalidCustomerException(String msg){
        super(msg);
    }

}
