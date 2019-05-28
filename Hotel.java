/*
  Author: Nathan Dunne
  Date: 19/05/2019
  Purpose: Operate a Hotel booking system concurrently.
  
  Code used and modified in part from:
  	https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html
  	https://www.geeksforgeeks.org/synchronized-in-java/
  	https://www.baeldung.com/java-synchronized
 */

import java.util.ArrayList;
import java.util.Arrays;

public class Hotel 
{
	private Object BookingLock = new Object(); // Create a lock object to lock booking.
	private HotelState State = new HotelState(); // Create an object to store the state of the hotel.

    // Hotel takes in all the number of rooms it has in use.
    public Hotel(int[] roomNums)
    {       
        synchronized(BookingLock)
        {
        	State.initState(roomNums);
        }    
    }
    
    // Create a booking with reference bookingRef for the room roomNum for each of the days specified in days.
    public boolean bookRoom(String bookingRef, int[] days, int roomNum)
    {
    	//System.out.println("Attempting to book room.:");
    	//System.out.println("Before booking lock");
    	
        synchronized(BookingLock)
        {
            if(roomBooked(days, roomNum))
            {
                return false;
            }
            else
            {
            	State.updateStateWithBooking(bookingRef, days, roomNum);
            	
                // Notify user of booking information.
                System.out.println("Room booked:");
                System.out.println(" Booking reference: " + bookingRef + "\n Room: " + roomNum + "\n On days: " + Arrays.toString(days));
                System.out.println();
                   
                return true;
            }
        }
    }
	
	// Returns true if the room roomNum is booked on any of the days specified in days, otherwise returns false.
    private boolean roomBooked(int[] days, int roomNum)
    {
        synchronized(BookingLock)
        {
            if(State.getDaysBookedInRoom(roomNum) == null)
            {
                return false;
            }

            ArrayList<Integer> daysBooked = new ArrayList<Integer>();
            
            if(State.isKeyValidRoomNumberDays(roomNum))
            {
            	// for each day booked at a given room number
                for(int dayBooked : State.getDaysBookedInRoom(roomNum)) 
                {
                	daysBooked.add(dayBooked); // add to the days to check.
                }

                // for each day in days
                for(int day : days)
                {
                	 if(daysBooked.contains(day)) 
                     {
                		 return true; // Room is booked at least once on given days.
                     }
                }   
            }

            return false; // Room is not booked on any given days.
        }
    }
    
    // The room booked under this booking reference becomes un-booked for the days of the booking.
    public void cancelBooking(String bookingRef) throws NoSuchBookingException
    {
    	//System.out.println("Attempting to cancel booking..:");
    	//System.out.println("Before cancel lock");
    	
        synchronized(BookingLock)
        {
            if(!State.isKeyValidBookingRefRooms(bookingRef))
            {
                throw new NoSuchBookingException(bookingRef);
            }
            else if(State.getRoomNumberByReference(bookingRef) != null)
            {
                int roomNum = State.getRoomNumberByReference(bookingRef); // Room number in question.
                
                int[] allDaysInRoom =  State.getDaysBookedInRoom(roomNum); // All day associated with the room.
                int[] daysToCancel = State.getDayByReference(bookingRef); // The days associated with the booking.
                
                int[] daysToReassignToRoom = new int[365]; // Days to re-assign to the room once done.
                
                int i = 0;
                // For each day in the days booked for a room.
                for(int day: allDaysInRoom)
                {
                	// For each day to cancel
                	for(int dayToCancel : daysToCancel)
                	{
                		// If the day does not need to be cancelled.
                		if(dayToCancel != day)
                		{
                			daysToReassignToRoom[i] = day; // Reassign that date.	
                		}
                	}
                	
                	i++;
                }
                
                State.UpdateStateWithCancellation(bookingRef, roomNum, daysToReassignToRoom);
            
                System.out.println("Cancelled booking reference: " + bookingRef + " on: " + Thread.currentThread().getName());
                System.out.println();
            }
        }
    }
	
	    // Optional method. To check multiple room numbers we can brute force and check one at a time with roomBooked().
    // Needs improvement.
    public boolean roomsBooked(int[] days, int[] roomNums)
    { 
        synchronized(BookingLock)
        {
        	for(int roomNum : roomNums)
        	{
        		if (roomBooked(days, roomNum))
        		{
        			return true;
        		}
        	}
        	
        	return false;
        } 	
    } 
    
    // Updates the booking with reference bookingRef so that it now refers to the specified roomNum for each of the days specified in days.
    public boolean updateBooking(String bookingRef, int[] days, int roomNum) throws NoSuchBookingException
    {
    	//System.out.println("Attempting to update booking..:");
    	//System.out.println("Before updating lock");
        
        synchronized(BookingLock)
        {
        	// If no booking is found under the given reference
            if(!State.isKeyValidBookingRefRooms(bookingRef))
            {
                throw new NoSuchBookingException(bookingRef);
            }

            // If the room is already booked on the days.
            if(roomBooked(days, roomNum))
            {
                System.out.println(Thread.currentThread().getName() + "\n Room "+ roomNum +" is already booked on days " + Arrays.toString(days) + " \n");
                
                return false;
            }        
            else
            {
                if(State.getRoomNumberByReference(bookingRef) != null)
                {
                    cancelBooking(bookingRef);// Cancel the given booking, freeing up the room.
                    bookRoom(bookingRef, days, roomNum); // Re-book with new updated values.
                    
                    System.out.println("Updated booking reference: " + bookingRef + " on: " + Thread.currentThread().getName() + " \n");
                }
            }
        }
        
        return true;
    }
    
    

}