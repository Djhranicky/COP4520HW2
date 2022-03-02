// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

class BirthdayParty{

    static Boolean declared = false;

    public static void declare(){
        declared = true;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        int numGuests;
        AtomicBoolean isCupcake = new AtomicBoolean(true);
        Random rand = new Random();
        int nextGuest;
        

        System.out.println("How many guests are attending the birthday party?");
        numGuests = sc.nextInt();

        Guest[] guests = new Guest[numGuests];
        guests[0] = new Guest(true, isCupcake, numGuests);
        Thread th = new Thread(guests[0]);
        th.start();
        for(int i = 1; i < numGuests; i++){
            guests[i] = new Guest(false, isCupcake, numGuests);
            th = new Thread(guests[i]);
            th.start();
        }

        
        int[] timesEntered = new int[numGuests];

        while(!declared){
            nextGuest = rand.nextInt(numGuests);
            guests[nextGuest].setState(Guest.State.INMAZE);
            timesEntered[nextGuest]++;
            while(guests[nextGuest].getState() == Guest.State.INMAZE){
                ;
            }
        }

        for(int i = 0; i < numGuests; i++){
            guests[i].setState(Guest.State.WIN);
        }

        System.out.println("All Guests have made it through");
        for(int i = 0; i < numGuests; i++){
            System.out.print(timesEntered[i] + " ");
        }
        System.out.println();

        sc.close();
    }
}