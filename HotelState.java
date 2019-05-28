/*
  Author: Nathan Dunne
  Date: 19/05/2019
  Purpose: Operate a Hotel booking system concurrently.
  
  Code used and modified in part from:
  	https://www.callicoder.com/java-hashmap/
  	https://www.w3schools.com/java/java_hashmap.asp
 */

import java.util.HashMap;

public class HotelState
{
	// Create a Hashmap that uses booking reference to room number as a key value pair.
	private HashMap<String, Integer> hmBookingReferenceToRoomNumber = new HashMap<String, Integer>();
	
	// Create a Hashmap that uses room number to days that room is booked as a key value pair.
    private HashMap<Integer, int[]> hmRoomNumberToDays = new HashMap<Integer, int[]>();
    
    // Create a Hashmap that uses booking reference to days as a key value pair.
    private HashMap<String, int[]> hmBookingReferenceToDays = new HashMap<String, int[]>();
    
    public void updateStateWithBooking(String bookingRef, int[] days, int roomNum)
    {
        hmBookingReferenceToRoomNumber.put(bookingRef, roomNum);
        hmRoomNumberToDays.put(roomNum, days);
        hmBookingReferenceToDays.put(bookingRef, days);
    }
    
    // Init the state of the hotel with initially un-booked rooms.
    public void initState(int[] roomNums)
    {
    	for(int roomNum : roomNums)
        {
        	hmRoomNumberToDays.put(roomNum, null); // Fill the hash map with the possible room numbers, give them no days booked initially.
        }
    }
    
    // Get the days associated with a room.
    public int[] getDaysBookedInRoom(int roomNum)
    {
       return hmRoomNumberToDays.get(roomNum);
    }
    
    // Get the room number associated with a booking reference.
    public Integer getRoomNumberByReference(String bookingRef)
    {
    	return hmBookingReferenceToRoomNumber.get(bookingRef);
    }
    
    // Get the days associated with a reference.
    public int[] getDayByReference(String bookingRef)
    {
    	return hmBookingReferenceToDays.get(bookingRef);
    }
    
    // Check if a room number is within our hashmap.
    public boolean isKeyValidRoomNumberDays(int roomNum)
    {
    	return hmRoomNumberToDays.containsKey(roomNum);
    }
    
    // Check if a booking reference is within within our hashmap.
    public boolean isKeyValidBookingRefRooms(String bookingRef)
    {
    	return hmBookingReferenceToRoomNumber.containsKey(bookingRef);
    }
    
    // Cancel the booking by removing the booking reference and re-assigning what days are assigned to the room outside of the given booking reference.
    public void UpdateStateWithCancellation(String bookingRef, int roomNum, int[] daysToReassignToRoom) 
    {
    	hmBookingReferenceToDays.remove(bookingRef); // Remove the booking reference
        hmBookingReferenceToRoomNumber.remove(bookingRef); // Remove the room number from the booking reference.
        hmRoomNumberToDays.put(roomNum, daysToReassignToRoom); // Enter in days un-effected by booking cancellation.
    }
}
