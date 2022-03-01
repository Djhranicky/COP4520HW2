// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

class BirthdayParty{
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        int numGuests;
        AtomicBoolean isCupcake = new AtomicBoolean(true);
        

        System.out.println("How many guests are attending the birthday party?");
        numGuests = sc.nextInt();

        Thread[] guests = new Thread[numGuests];
        guests[0] = new Thread(new Guest(true, isCupcake, numGuests));
        for(int i = 1; i < numGuests; i++){
            guests[i] = new Thread(new Guest(false, isCupcake, numGuests));
        }

        sc.close();
    }
}