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
    int numGuests;
    Boolean leader;
    AtomicBoolean cupcake;


    // Simple constructor for the Guests.
    public Guest(Boolean leader, AtomicBoolean cupcake, int numGuests){
        this.leader = leader;
        this.cupcake = cupcake;
        this.numGuests = numGuests;
        this.state = new AtomicInteger(WAITING);
        this.replaced = false;
        this.count = 0;
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
        while(state.get() != WIN){
            
            // Logic for the leader to follow, allows them to count how many
            // people have entered the maze
            if(leader){
                // System.out.println(count);
                if(state.get() == WAITING){
                    continue;
                }
                else if(state.get() == INMAZE){

                    // The leader eats a cupcake if it is there and increments
                    // the count by one each time.
                    if(count < numGuests && cupcake.compareAndSet(true, false)){
                        count++;
                    }

                    // Declare that all guests have entered the maze at least
                    // once when they have eaten a number of cupcakes equal to
                    // the number of guests.
                    else if(count == numGuests){
                        BirthdayParty.declare();
                    }

                    // Wait again after leaving the maze if they haven't won.
                    state.compareAndSet(INMAZE, WAITING);
                }
            }

            // Logic for the other guests to follow.
            else{
                if(state.get() == WAITING){
                    continue;
                }
                else if(state.get() == INMAZE){

                    // Replace the cupcake only if they have not already
                    // replaced it before.
                    if(replaced == false && cupcake.compareAndSet(false, true)){
                        replaced = true;
                    }

                    // Wait again after leaving the maze if they haven't won.
                    state.compareAndSet(INMAZE, WAITING);
                }
            }
        }
    }
}
