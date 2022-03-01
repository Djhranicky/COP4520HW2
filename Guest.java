import java.util.concurrent.atomic.AtomicBoolean;

public class Guest implements Runnable{

    enum State{
        WAITING, INMAZE, WIN
    };
    
    private State state;
    private Boolean replaced;
    private int count;
    int numGuests;
    Boolean leader;
    AtomicBoolean cupcake;

    public Guest(Boolean leader, AtomicBoolean cupcake, int numGuests){
        this.leader = leader;
        this.cupcake = cupcake;
        this.numGuests = numGuests;
        this.state = State.WAITING;
        this.replaced = false;
    }

    public void setState(State state){
        this.state = state;
    }

    @Override
    public void run(){
        while(state != State.WIN){
            if(leader){
                if(count)
            }
            else{
                if(state == State.WAITING){
                    continue;
                }
                else if(state == State.INMAZE){
                    if(replaced == false && cupcake.compareAndSet(false, true)){
                        replaced = true;
                    }
                }
            }
        }
    }
}
