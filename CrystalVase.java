// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

class CrystalVase{
    public static void main(String[] args){
        

        Scanner sc = new Scanner(System.in);
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
        Thread th;

        // Read in number of guests.
        System.out.println("Enter the number of guests:");
        int numGuests = sc.nextInt();


        // Create the viewer objects. Starting is done separately so the guests
        // array is populated before any threads start runnung, preventing
        // null accessing.
        Viewer[] guests = new Viewer[numGuests];
        for(int i = 0; i < numGuests; i++){
            guests[i] = new Viewer(i, queue, guests);
        }
        for(int i = 0; i < numGuests; i++){
            th = new Thread(guests[i]);
            th.start();
        }


        // Allow some threads to populate the queue before polling the first
        // one. (People must mingle before they can see the vase!)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // Spinlock to ensure that the queue has at lease one entry.
        while(queue.isEmpty()){
            ;
        }


        // Bring the first guest into the viewing room.
        int firstGuest = queue.poll();
        guests[firstGuest].setState(Viewer.State.VIEWING);


        // Spinlock to wait for the queue to empty.
        while(!queue.isEmpty()){
            ;
        }


        // Send the guests home once no one want to see the vase.
        for(int i = 0; i < numGuests; i++){
            guests[i].setState(Viewer.State.HOME);
        }

        sc.close();
    }
}