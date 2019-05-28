import java.util.Random;

/*
Author: Nathan Dunne
Date: 19/05/2019
Purpose: Test the concurrent Hotel program.

Code used and modified in part from:
	http://tutorials.jenkov.com/java-concurrency/creating-and-starting-threads.html
*/

public class HotelThreadTester 
{    
    public static void main (String [] args) throws InterruptedException 
    {
    	testHotel(); // Run the test function.
    }
    
    public static void testHotel()
    {
    	// Set up test parameters.
    	int[] arrRoomNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    	Hotel HabboHotel = new Hotel(arrRoomNumbers);
    	
    	// Use some RNG to try break concurrency.
    	Random rand = new Random();
    	
    	int numberOfRooms = arrRoomNumbers.length;
       
    	// Set some days for each "person" to book.
        int[] daysDunne = {1, 2, 3};
        int[] daysOBrien = {1, 3, 8};
        int[] daysCarrew = {4, 7};
        int[] daysDawson = {3, 7, 8};
        int[] daysRizla = {5, 9, 1};
        
        // Set a run amount.
        int runs = 100;
        
        for(int i = 0; i< runs; i++)
        {
	        // Book some rooms, use random to get a random room to book.
	        Thread test1 = new Thread(new Runnable() 
	        {
	            public void run() 
	            {
	                HabboHotel.bookRoom("Dunne", daysDunne, rand.nextInt(numberOfRooms));
	            }
	        });
	        test1.start(); 
	        
	        
	        Thread test2 = new Thread(new Runnable() 
	        {
	            public void run() 
	            {
	                HabboHotel.bookRoom("OBrien", daysOBrien, rand.nextInt(numberOfRooms));
	            }
	        });
	        test2.start(); 
	        
	        
	        Thread test3 = new Thread(new Runnable() 
	        {
	            public void run() 
	            {
	                HabboHotel.bookRoom("Carrew", daysCarrew, rand.nextInt(numberOfRooms));
	            }
	        });
	        test3.start();
	        
	        
	        Thread test4 = new Thread(new Runnable() 
	        {
	            public void run() 
	            {
	                HabboHotel.bookRoom("Dawson", daysDawson, rand.nextInt(numberOfRooms));
	            }
	        });
	        test4.start(); 
	        
	        // Book multiple rooms within one thread.
	        Thread test5 = new Thread(new Runnable() 
	        {
	            public void run() 
	            {
	                HabboHotel.bookRoom("Dawson", daysDawson, rand.nextInt(numberOfRooms));
	                HabboHotel.bookRoom("Dunne", daysDunne, rand.nextInt(numberOfRooms));
	                HabboHotel.bookRoom("Carrew", daysDunne, rand.nextInt(numberOfRooms));
	                HabboHotel.bookRoom("Carrew", daysDunne, rand.nextInt(numberOfRooms));
	                HabboHotel.bookRoom("Rizla", daysRizla, rand.nextInt(numberOfRooms));
	            }
	        });
	        test5.start(); 
	        
	        
	        // Cancel and update bookings.
	        Thread test6 = new Thread(new Runnable() 
	        {
	            public void run() 
	            {        
	                try 
	                {
	                    HabboHotel.updateBooking("Carrew", daysCarrew, rand.nextInt(numberOfRooms));
	                    HabboHotel.cancelBooking("Dawson");
	                    HabboHotel.cancelBooking("OBrien");
	                    HabboHotel.updateBooking("Dunne", daysCarrew, rand.nextInt(numberOfRooms));
	                } 
	                catch(NoSuchBookingException exception) 
	                {
	                    System.out.println(exception.getMessage());
	                }
	            }
	        });
	        test6.start();
        }
    }
}