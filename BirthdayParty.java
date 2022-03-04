// David Hranicky
// COP4520

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


class BirthdayParty{

    // Declared tracks if the leader says that all guests went through the 
    // maze, and visited verifies which guests have entered
    static Boolean declared = false;
    static Boolean visited[];


    // Static function the leader uses to tell the Minotaur that everyone 
    // has gone through
    public static void declare(){
        declared = true;
    }


    // Static function the guests use to tell the Minotaur that they have 
    // entered the labyrinth
    public static void haveVisited(int guestNum){
        visited[guestNum] = true;
    }

    public static void main(String[] args){

        // isCupcake keeps track of whether a cupcake is present at the end
        // of the labyrinth.
        // mazeOccupied tracks if the maze is occupied.
        // allGuests is used to verify if all of the guests have entered.
        Scanner sc = new Scanner(System.in);
        int numGuests;
        AtomicBoolean isCupcake = new AtomicBoolean(true);
        AtomicBoolean mazeOccupied = new AtomicBoolean(false);
        Boolean allGuests = true;
        

        // Read in number of guests.
        System.out.println("How many guests are attending the birthday party?");
        numGuests = sc.nextInt();


        // Used to track which guests have entered the maze.
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


        // Randomly chooses one guest to enter the labyrinth until the leader
        // says all guests have entered.
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


        // Once the game is over, let all guests know they have won.
        for(int i = 0; i < numGuests; i++){
            guests[i].setState(Guest.WIN);
        }

        // Notify the waiting threads to allow them to leave the game.
        synchronized (mazeOccupied){
            System.out.println("Waiting for everyone to leave");
            while(mazeOccupied.get() == true){
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