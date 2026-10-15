package kz.agzam.carwash.api;

public class BookingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
