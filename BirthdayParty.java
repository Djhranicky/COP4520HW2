// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;


class BirthdayParty{

    // Global variable to track if a guest has declared that all guests have
    // gone through the labyrinth.
    static Boolean declared = false;

    public static void declare(){
        declared = true;
    }

    public static void main(String[] args){

        // isCupcake keeps track of whether a cupcake is present at the end
        // of the labyrinth
        Scanner sc = new Scanner(System.in);
        int numGuests;
        AtomicBoolean isCupcake = new AtomicBoolean(true);
        Random rand = new Random();
        int nextGuest;
        Boolean allGuests = true;
        

        // Read in number of guests.
        System.out.println("How many guests are attending the birthday party?");
        numGuests = sc.nextInt();


        // Create the guest objects. The first guest automatically becomes the
        // leader.
        Guest[] guests = new Guest[numGuests];
        guests[0] = new Guest(true, isCupcake, numGuests, Guest.Problem.PARTY);
        Thread th = new Thread(guests[0]);
        th.start();
        for(int i = 1; i < numGuests; i++){
            guests[i] = new Guest(false, isCupcake, numGuests, Guest.Problem.PARTY);
            th = new Thread(guests[i]);
            th.start();
        }

        
        // Minotaur keeping track of the number of times each guest enters the
        // labyrinth for proof of correctness.
        int[] timesEntered = new int[numGuests];


        // The game state, which keeps running as long as the leader has not
        // declared that everyone has gone through. 
        while(!declared){
            nextGuest = rand.nextInt(numGuests);
            guests[nextGuest].setState(Guest.State.INMAZE);
            timesEntered[nextGuest]++;
            while(guests[nextGuest].getState() == Guest.State.INMAZE){
                ;
            }
        }


        // Once the game is over, terminate the threads by setting all of their
        // states to WIN, exiting the while loop.
        for(int i = 0; i < numGuests; i++){
            guests[i].setState(Guest.State.WIN);
        }


        // Checks if all of the guests entered the labyrinth and prints the
        // result.
        for(int i = 0; i < numGuests; i++){
            allGuests = allGuests && (timesEntered[i] > 0);
        }
        if(allGuests){
            System.out.println("All Guests entered the labyrinth");
        } else {
            System.out.println("At least one guest did not enter the labyrinth");
        }


        // Prints the number of times each guest entered the labyrinth.
        for(int i = 0; i < numGuests; i++){
            System.out.println("Guest "+i+" entered "+timesEntered[i]+" times.");
        }

        sc.close();
    }
}