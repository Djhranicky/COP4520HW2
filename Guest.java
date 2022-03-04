import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Guest implements Runnable{


    // Enum to describe which state of the game a guest is in. WAITING is when
    // they are waiting to be called into the labyrinth. INMAZE is when they 
    // are solving the maze, and WIN is after the leader has declared that all
    // guests have entered the maze at least once.
    public static final int WAITING = 0;
    public static final int INMAZE = 1;
    public static final int WIN = 2;

    
    // Local variables. State keeps track of the current game state a guest
    // is in. Replaced indicates if a guest has repllaced the cupcake before.
    // Count allows the leader to count the number of cupcakes they have eaten.
    // NumGuests is the total number of guests, leader is true if a guest is 
    // the leader, and cupcake keeps track of the state of the cupcake. True
    // if it is there and false if it is not there.
    private AtomicInteger state;
    private Boolean replaced;
    private int count;
    private int guestNum;
    int numGuests;
    Boolean leader;
    AtomicBoolean cupcake;
    AtomicBoolean mazeOccupied;


    // Simple constructor for the Guests.
    public Guest(int i, Boolean leader, AtomicBoolean cupcake, int numGuests, AtomicBoolean mazeOccupied){
        this.leader = leader;
        this.cupcake = cupcake;
        this.numGuests = numGuests;
        this.state = new AtomicInteger(WAITING);
        this.replaced = false;
        this.count = 0;
        this.mazeOccupied = mazeOccupied;
        this.guestNum = i;
    }


    // Returns the current state of a guest.
    public int getState(){
        return this.state.get();
    }


    // Allows the Minotaur to say the guests have won.
    public void setState(int state){
        this.state.set(state);
    }


    // Implements a winning strategy for the guests.
    @Override
    public void run(){
        
        synchronized (mazeOccupied){

            while(mazeOccupied.get() == false){

                // If the guest has won, leave the game.
                if(state.get() == WIN){
                    break;
                }

                // Wait to be called into the maze.
                try{
                    mazeOccupied.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Mark the maze as occupied once summoned.
                mazeOccupied.set(true);
                BirthdayParty.haveVisited(guestNum);

                // Logic for the leader. If there is a cupcake, eat it and
                // increment the count. If the count reaches the number of 
                // guests, then everyone has entered.
                if(leader){
                    if(count < numGuests && cupcake.compareAndSet(true, false)){
                        count++;
                    }

                    if(count == numGuests){
                        BirthdayParty.declare();
                    }
                } 
                
                // Logic for members. If there is no cupcake and they have not
                // replaced it before, replace it one time.
                else {
                    if(!replaced && cupcake.compareAndSet(false, true)){
                        replaced = true;
                    }
                }
                
                // Mark the maze as open when leaving.
                mazeOccupied.set(false);
            }
        }
    }
}
