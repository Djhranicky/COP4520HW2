import java.util.concurrent.atomic.AtomicBoolean;

public class Guest implements Runnable{


    // Enum to describe which state of the game a guest is in. WAITING is when
    // they are waiting to be called into the labyrinth. INMAZE is when they 
    // are solving the maze, and WIN is after the leader has declared that all
    // guests have entered the maze at least once.
    enum State{
        WAITING, INMAZE, WIN
    };

    
    // Local variables. State keeps track of the current game state a guest
    // is in. Replaced indicates if a guest has repllaced the cupcake before.
    // Count allows the leader to count the number of cupcakes they have eaten.
    // NumGuests is the total number of guests, leader is true if a guest is 
    // the leader, and cupcake keeps track of the state of the cupcake. True
    // if it is there and false if it is not there.
    private State state;
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
        this.state = State.WAITING;
        this.replaced = false;
        this.count = 0;
    }


    // Returns the current state of a guest.
    public State getState(){
        return this.state;
    }


    // Allows the Minotaur to call a guest into the labyrinth.
    public void setState(State state){
        this.state = state;
    }


    // Implements a winning strategy for the guests.
    @Override
    public void run(){
        
        // Guests will always run unless they win the game.
        while(state != State.WIN){
            
            // Logic for the leader to follow, allows them to count how many
            // people have entered the maze
            if(leader){
                if(state == State.WAITING){
                    continue;
                }
                else if(state == State.INMAZE){

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
                    if(state != State.WIN){
                        state = State.WAITING;
                    }
                }
            }

            // Logic for the other guests to follow.
            else{
                if(state == State.WAITING){
                    continue;
                }
                else if(state == State.INMAZE){

                    // Replace the cupcake only if they have not already
                    // replaced it before.
                    if(replaced == false && cupcake.compareAndSet(false, true)){
                        replaced = true;
                    }

                    // Wait again after leaving the maze if they haven't won.
                    if(state != State.WIN){
                        state = State.WAITING;
                    }
                }
            }
        }
    }
}
