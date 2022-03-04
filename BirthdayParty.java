// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


class BirthdayParty{

    // Global variable to track if a guest has declared that all guests have
    // gone through the labyrinth.
    static Boolean declared = false;
    static Boolean visited[];

    public static void declare(){
        declared = true;
    }

    public static void haveVisited(int guestNum){
        visited[guestNum] = true;
    }

    public static void main(String[] args){

        // isCupcake keeps track of whether a cupcake is present at the end
        // of the labyrinth
        Scanner sc = new Scanner(System.in);
        int numGuests;
        AtomicBoolean isCupcake = new AtomicBoolean(true);
        AtomicBoolean mazeOccupied = new AtomicBoolean(false);
        Boolean allGuests = true;
        

        // Read in number of guests.
        System.out.println("How many guests are attending the birthday party?");
        numGuests = sc.nextInt();

        visited = new Boolean[numGuests];


        // Create the guest objects. The first guest automatically becomes the
        // leader.
        Guest[] guests = new Guest[numGuests];
        guests[0] = new Guest(0, true, isCupcake, numGuests, mazeOccupied);
        Thread th = new Thread(guests[0]);
        th.start();
        for(int i = 1; i < numGuests; i++){
            guests[i] = new Guest(i, false, isCupcake, numGuests, mazeOccupied);
            th = new Thread(guests[i]);
            th.start();
        }


        while(!declared){
            synchronized(mazeOccupied){
                while(mazeOccupied.get() == true){
                    try{
                       mazeOccupied.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mazeOccupied.notify();
            }
        }


        // Once the game is over, terminate the threads by setting all of their
        // states to WIN, exiting the while loop.
        for(int i = 0; i < numGuests; i++){
            guests[i].setState(Guest.WIN);
        }

        synchronized (mazeOccupied){
            System.out.println("Waiting for everyone to leave");
            for(int i = 0; i < numGuests && mazeOccupied.get() == true; i++){
            try{
                mazeOccupied.wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
            mazeOccupied.notifyAll();
        }


        // Checks if all of the guests entered the labyrinth and prints the
        // result.
        for(int i = 0; i < numGuests; i++){
            allGuests = allGuests && visited[i];
        }
        if(allGuests){
            System.out.println("All Guests entered the labyrinth");
        } else {
            System.out.println("At least one guest did not enter the labyrinth");
        }

        sc.close();
    }
}