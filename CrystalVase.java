// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Random;

class CrystalVase{
    public static void main(String[] args){
        
        Scanner sc = new Scanner(System.in);
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
        Thread th;

        System.out.println("Enter the number of guests:");
        int numGuests = sc.nextInt();

        Viewer[] guests = new Viewer[numGuests];
        for(int i = 0; i < numGuests; i++){
            guests[i] = new Viewer(i, queue, guests);
        }
        for(int i = 0; i < numGuests; i++){
            th = new Thread(guests[i]);
            th.start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int firstGuest = queue.poll();
        guests[firstGuest].setState(Viewer.State.VIEWING);

        while(!queue.isEmpty()){
            ;
        }

        for(int i = 0; i < numGuests; i++){
            guests[i].setState(Viewer.State.HOME);
        }

        sc.close();
    }
}