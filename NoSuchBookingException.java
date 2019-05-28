/*
    Hotel Room Booking System.
*/

public class NoSuchBookingException extends Exception {
    public NoSuchBookingException (String bookingRef) {
	super("There is no booking with reference " + bookingRef);
    }
}