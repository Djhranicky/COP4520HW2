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


    // Allows the Minotaur to call a guest into the labyrinth.
    public void setState(int state){
        this.state.set(state);
    }


    // Implements a winning strategy for the guests.
    @Override
    public void run(){
        
        // Guests will always run unless they win the game.
        synchronized (mazeOccupied){

            while(mazeOccupied.get() == false){
                if(state.get() == WIN){
                    break;
                }
                    try{
                        mazeOccupied.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                mazeOccupied.set(true);
                BirthdayParty.haveVisited(guestNum);

                if(leader){
                    if(count < numGuests && cupcake.compareAndSet(true, false)){
                        count++;
                    }

                    if(count == numGuests){
                        BirthdayParty.declare();
                    }
                } else {
                    if(replaced == false && cupcake.compareAndSet(false, true)){
                        replaced = true;
                    }
                }
                
                mazeOccupied.set(false);
            }
        }
    }
}
